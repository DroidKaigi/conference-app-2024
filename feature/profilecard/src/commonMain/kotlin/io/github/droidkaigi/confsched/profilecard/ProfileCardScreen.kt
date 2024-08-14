package io.github.droidkaigi.confsched.profilecard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.add_image
import conference_app_2024.feature.profilecard.generated.resources.add_validate_format
import conference_app_2024.feature.profilecard.generated.resources.enter_validate_format
import conference_app_2024.feature.profilecard.generated.resources.icon_share
import conference_app_2024.feature.profilecard.generated.resources.image
import conference_app_2024.feature.profilecard.generated.resources.link
import conference_app_2024.feature.profilecard.generated.resources.nickname
import conference_app_2024.feature.profilecard.generated.resources.occupation
import conference_app_2024.feature.profilecard.generated.resources.profile_card
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardScreenTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardScreenTheme
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardTheme
import io.github.droidkaigi.confsched.profilecard.component.PhotoPickerButton
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val profileCardScreenRoute = "profilecard"

const val ProfileCardEditScreenTestTag = "ProfileCardEditScreenTestTag"
const val ProfileCardNicknameTextFieldTestTag = "ProfileCardNicknameTextFieldTestTag"
const val ProfileCardOccupationTextFieldTestTag = "ProfileCardOccupationTextFieldTestTag"
const val ProfileCardLinkTextFieldTestTag = "ProfileCardLinkTextFieldTestTag"
const val ProfileCardSelectImageButtonTestTag = "ProfileCardSelectImageButtonTestTag"
const val ProfileCardCreateButtonTestTag = "ProfileCardCreateButtonTestTag"
const val ProfileCardCardScreenTestTag = "ProfileCardCardScreenTestTag"
const val ProfileCardEditButtonTestTag = "ProfileCardEditButtonTestTag"

fun NavGraphBuilder.profileCardScreen(contentPadding: PaddingValues) {
    composable(profileCardScreenRoute) {
        ProfileCardScreen(contentPadding)
    }
}

fun NavController.navigateProfileCardScreen() {
    navigate(profileCardScreenRoute) {
        popUpTo(checkNotNull(graph.findStartDestination().route)) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

internal sealed interface ProfileCardUiState {
    data class Edit(
        val nickname: String = "",
        val occupation: String = "",
        val link: String = "",
        val image: String? = null,
        val theme: ProfileCardTheme = ProfileCardTheme.Iguana,
    ) : ProfileCardUiState

    data class Card(
        val nickname: String,
        val occupation: String,
        val link: String,
        val image: String,
        val theme: ProfileCardTheme,
    ) : ProfileCardUiState
}

internal enum class ProfileCardUiType {
    Loading,
    Edit,
    Card,
}

internal data class ProfileCardScreenState(
    val isLoading: Boolean,
    val editUiState: ProfileCardUiState.Edit,
    val cardUiState: ProfileCardUiState.Card?,
    val uiType: ProfileCardUiType,
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun ProfileCardScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    ProfileCardScreen(
        contentPadding = contentPadding,
        modifier = modifier,
        rememberEventEmitter(),
    )
}

@Composable
internal fun ProfileCardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    eventEmitter: EventEmitter<ProfileCardScreenEvent> = rememberEventEmitter(),
    uiState: ProfileCardScreenState = profileCardScreenPresenter(eventEmitter),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val layoutDirection = LocalLayoutDirection.current

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(
            left = contentPadding.calculateLeftPadding(layoutDirection),
            top = contentPadding.calculateTopPadding(),
            right = contentPadding.calculateRightPadding(layoutDirection),
            bottom = contentPadding.calculateBottomPadding(),
        ),
    ) { padding ->
        when (uiState.uiType) {
            ProfileCardUiType.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(padding).fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
            }

            ProfileCardUiType.Edit -> {
                EditScreen(
                    uiState = uiState.editUiState,
                    onClickCreate = {
                        eventEmitter.tryEmit(EditScreenEvent.Create(it))
                    },
                    contentPadding = padding,
                )
            }

            ProfileCardUiType.Card -> {
                if (uiState.cardUiState == null) return@Scaffold
                CardScreen(
                    uiState = uiState.cardUiState,
                    onClickEdit = {
                        eventEmitter.tryEmit(CardScreenEvent.Edit)
                    },
                    onClickShareProfileCard = {
                        eventEmitter.tryEmit(CardScreenEvent.Share)
                    },
                    contentPadding = padding,
                    isCreated = true,
                )
            }
        }
        if (uiState.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(padding).fillMaxSize(),
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
internal fun EditScreen(
    uiState: ProfileCardUiState.Edit,
    onClickCreate: (ProfileCard.Exists) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    var nickname by remember { mutableStateOf(uiState.nickname) }
    var occupation by remember { mutableStateOf(uiState.occupation) }
    var link by remember { mutableStateOf(uiState.link) }
    var imageByteArray: ByteArray? by remember { mutableStateOf(uiState.image?.decodeBase64Bytes()) }
    val image by remember { derivedStateOf { imageByteArray?.toImageBitmap() } }

    val nicknameValidationErrorString = stringResource(
        ProfileCardRes.string.enter_validate_format,
        stringResource(ProfileCardRes.string.nickname),
    )
    val occupationValidationErrorString = stringResource(
        ProfileCardRes.string.enter_validate_format,
        stringResource(ProfileCardRes.string.occupation),
    )
    val linkValidationErrorString = stringResource(
        ProfileCardRes.string.enter_validate_format,
        stringResource(ProfileCardRes.string.link),
    )
    val imageValidationErrorString = stringResource(
        ProfileCardRes.string.add_validate_format,
        stringResource(ProfileCardRes.string.image),
    )

    val nicknameError by remember {
        derivedStateOf {
            mutableStateOf(
                if (nickname.isNotEmpty()) {
                    ""
                } else {
                    nicknameValidationErrorString
                }
            )
        }
    }
    val occupationError by remember {
        derivedStateOf {
            mutableStateOf(
                if (occupation.isNotEmpty()) {
                    ""
                } else {
                    occupationValidationErrorString
                }
            )
        }
    }
    val linkError by remember {
        derivedStateOf {
            mutableStateOf(
                if (link.isNotEmpty()) {
                    ""
                } else {
                    linkValidationErrorString
                }
            )
        }
    }
    val imageError by remember {
        derivedStateOf {
            mutableStateOf(
                if (image != null) {
                    ""
                } else {
                    imageValidationErrorString
                }
            )
        }
    }

    val isValidInputs by remember {
        derivedStateOf {
            nickname.isNotEmpty() && occupation.isNotEmpty() && link.isNotEmpty() && image != null
        }
    }

    Column(
        modifier = modifier
            .testTag(ProfileCardEditScreenTestTag)
            .padding(contentPadding),
    ) {
        Text("ProfileCardEdit")
        ValidationTextField(
            value = nickname,
            labelName = stringResource(ProfileCardRes.string.nickname),
            errorMessage = nicknameError.value,
            isError = nickname.isEmpty(),
            onValueChange = {
                nickname = it
            },
            modifier = Modifier.testTag(ProfileCardNicknameTextFieldTestTag),
        )
        ValidationTextField(
            value = occupation,
            labelName = stringResource(ProfileCardRes.string.occupation),
            errorMessage = occupationError.value,
            isError = occupation.isEmpty(),
            onValueChange = {
                occupation = it
            },
            modifier = Modifier.testTag(ProfileCardOccupationTextFieldTestTag),
        )
        ValidationTextField(
            value = link,
            labelName = stringResource(ProfileCardRes.string.link),
            isError = link.isEmpty(),
            onValueChange = {
                link = it
            },
            errorMessage = linkError.value,
            modifier = Modifier.testTag(ProfileCardLinkTextFieldTestTag),
        )
        Column {
            Text(
                text = stringResource(ProfileCardRes.string.image),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            image?.let {
                Box {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(
                                top = 24.dp,
                                end = 24.dp,
                            )
                            .align(Alignment.BottomStart),
                    )
                    IconButton(
                        onClick = { imageByteArray = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        colors = IconButtonDefaults
                            .iconButtonColors()
                            .copy(containerColor = Color(0xFF414849)),
                    ) {
                        Icon(Icons.Default.Close, null)
                    }
                }
            } ?: run {
                ValidationPhotoPickerButton(
                    onSelectedImage = { imageByteArray = it },
                    errorMessage = imageError.value,
                    modifier = Modifier.testTag(ProfileCardSelectImageButtonTestTag),
                )
            }
        }
        Button(
            onClick = {
                onClickCreate(
                    ProfileCard.Exists(
                        nickname = nickname,
                        occupation = occupation,
                        link = link,
                        image = imageByteArray?.toBase64() ?: "",
                        theme = uiState.theme,
                    ),
                )
            },
            enabled = isValidInputs,
            modifier = Modifier.testTag(ProfileCardCreateButtonTestTag),
        ) {
            Text("Create")
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
private fun ByteArray.toBase64(): String = Base64.encode(this)

@OptIn(ExperimentalEncodingApi::class)
private fun String.decodeBase64Bytes(): ByteArray = Base64.decode(this)

@Composable
internal fun CardScreen(
    uiState: ProfileCardUiState.Card,
    onClickEdit: () -> Unit,
    onClickShareProfileCard: () -> Unit,
    modifier: Modifier = Modifier,
    isCreated: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    ProvideProfileCardScreenTheme(uiState.theme.toString()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(LocalProfileCardScreenTheme.current.primaryColor)
                .testTag(ProfileCardCardScreenTestTag)
                .padding(contentPadding),
        ) {
            Text(
                text = stringResource(ProfileCardRes.string.profile_card),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp),
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                FlipCard(
                    uiState = uiState,
                    isCreated = isCreated,
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { onClickShareProfileCard() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                ) {
                    Icon(
                        painter = painterResource(ProfileCardRes.drawable.icon_share),
                        contentDescription = "Share",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "共有する",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Black,
                    )
                }
                Spacer(Modifier.height(9.dp))
                Text(
                    text = "編集する",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onClickEdit() }
                        .testTag(ProfileCardEditButtonTestTag),
                )
            }
        }
    }
}

@Composable
private fun ValidationTextField(
    value: String,
    labelName: String,
    errorMessage: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val indicatorColor = if (isError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        }
        Text(
            text = labelName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = value,
            textStyle = MaterialTheme.typography.bodyLarge,
            onValueChange = onValueChange,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = indicatorColor,
                unfocusedIndicatorColor = indicatorColor,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
            )
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 4.dp,
                        end = 16.dp,
                    )
            )
        }
    }
}

@Composable
private fun ValidationPhotoPickerButton(
    onSelectedImage: (ByteArray) -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PhotoPickerButton(
            onSelectedImage = { onSelectedImage(it) },
            modifier = Modifier.padding(bottom = 20.dp),
        ) {
            Text(stringResource(ProfileCardRes.string.add_image))
        }
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}
