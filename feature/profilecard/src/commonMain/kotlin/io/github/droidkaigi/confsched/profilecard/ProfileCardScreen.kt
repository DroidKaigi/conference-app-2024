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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.icon_share
import conference_app_2024.feature.profilecard.generated.resources.profile_card
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardScreenTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardScreenTheme
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardTheme
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.ktor.util.decodeBase64Bytes
import kotlinx.coroutines.CoroutineScope
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
        val occupation: String? = null,
        val link: String? = null,
        val image: String? = null,
        val theme: ProfileCardTheme = ProfileCardTheme.Iguana,
    ) : ProfileCardUiState

    data class Card(
        val nickname: String,
        val occupation: String?,
        val link: String?,
        val image: String?,
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

@OptIn(ExperimentalStdlibApi::class)
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
    var isOpenDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberSingleImagePickerLauncher {
        with(it.toImageBitmap()) {
            if (height != width) {
                // If the image is not square, throw an error.
                // Ideally, we would like to crop the image, but this is currently on hold due to the difficulty of implementing it by KMP.
                isOpenDialog = true
            } else {
                imageByteArray = it
            }
        }
    }

    if (isOpenDialog) {
        AlertDialog(
            onDismissRequest = { isOpenDialog = false },
            title = { Text("Error") },
            text = { Text("The image is not square.") },
            confirmButton = {
                Button(
                    onClick = { isOpenDialog = false },
                ) {
                    Text("OK")
                }
            },
        )
    }

    Column(
        modifier = modifier
            .testTag(ProfileCardEditScreenTestTag)
            .padding(contentPadding),
    ) {
        Text("ProfileCardEdit")
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            placeholder = { Text("Nickname") },
            modifier = Modifier.testTag(ProfileCardNicknameTextFieldTestTag),
        )
        TextField(
            value = occupation ?: "",
            onValueChange = { occupation = it },
            placeholder = { Text("Occupation") },
            modifier = Modifier.testTag(ProfileCardOccupationTextFieldTestTag),
        )
        TextField(
            value = link ?: "",
            onValueChange = { link = it },
            placeholder = { Text("Link") },
            modifier = Modifier.testTag(ProfileCardLinkTextFieldTestTag),
        )
        Button(
            onClick = {
                imagePicker.launch()
            },
            modifier = Modifier.testTag(ProfileCardSelectImageButtonTestTag),
        ) {
            Text("画像を選択")
        }
        imageByteArray?.let {
            Image(it.toImageBitmap(), null)
        }
        Button(
            onClick = {
                onClickCreate(
                    ProfileCard.Exists(
                        nickname = nickname,
                        occupation = occupation,
                        link = link,
                        image = imageByteArray?.toBase64(),
                        theme = uiState.theme,
                    ),
                )
            },
            modifier = Modifier.testTag(ProfileCardCreateButtonTestTag),
        ) {
            Text("Create")
        }
    }
}

@Composable
fun rememberSingleImagePickerLauncher(
    scope: CoroutineScope = rememberCoroutineScope(),
    onResult: (ByteArray) -> Unit,
) = rememberImagePickerLauncher(
    selectionMode = SelectionMode.Single,
    scope = scope,
    onResult = { byteArrays ->
        byteArrays.firstOrNull()?.let {
            onResult(it)
        }
    },
)

@OptIn(ExperimentalEncodingApi::class)
fun ByteArray.toBase64(): String = Base64.encode(this)

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
