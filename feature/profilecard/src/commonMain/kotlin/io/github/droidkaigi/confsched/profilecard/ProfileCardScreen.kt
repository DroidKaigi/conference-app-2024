package io.github.droidkaigi.confsched.profilecard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.add_image
import conference_app_2024.feature.profilecard.generated.resources.create_card
import conference_app_2024.feature.profilecard.generated.resources.icon_share
import conference_app_2024.feature.profilecard.generated.resources.image
import conference_app_2024.feature.profilecard.generated.resources.link_text
import conference_app_2024.feature.profilecard.generated.resources.nick_name
import conference_app_2024.feature.profilecard.generated.resources.occupation
import conference_app_2024.feature.profilecard.generated.resources.optional_input
import conference_app_2024.feature.profilecard.generated.resources.profile_card
import conference_app_2024.feature.profilecard.generated.resources.profile_card_edit_description
import conference_app_2024.feature.profilecard.generated.resources.profile_card_title
import conference_app_2024.feature.profilecard.generated.resources.select_theme
import conference_app_2024.feature.profilecard.generated.resources.theme
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardScreenTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardScreenTheme
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardTheme
import io.github.droidkaigi.confsched.profilecard.component.PhotoPickerButton
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.component.AnimatedTextTopAppBar
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
        val theme: ProfileCardTheme = ProfileCardTheme.entries.first(),
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

@OptIn(ExperimentalMaterial3Api::class)
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var imageByteArray: ByteArray? by remember { mutableStateOf(uiState.image?.decodeBase64Bytes()) }
    val image by remember { derivedStateOf { imageByteArray?.toImageBitmap() } }
    var selectedTheme by remember { mutableStateOf(uiState.theme) }

    Scaffold(
        modifier = modifier.testTag(ProfileCardEditScreenTestTag).padding(contentPadding),
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(ProfileCardRes.string.profile_card_title),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Text(stringResource(ProfileCardRes.string.profile_card_edit_description))

            InputColumn(
                label = stringResource(ProfileCardRes.string.nick_name),
                value = nickname,
                isOptional = false,
                testTag = ProfileCardNicknameTextFieldTestTag,
                onValueChanged = { nickname = it },
            )
            InputColumn(
                label = stringResource(ProfileCardRes.string.occupation),
                value = occupation ?: "",
                testTag = ProfileCardOccupationTextFieldTestTag,
                onValueChanged = { occupation = it },
            )
            InputColumn(
                label = stringResource(ProfileCardRes.string.link_text),
                value = link ?: "",
                testTag = ProfileCardLinkTextFieldTestTag,
                onValueChanged = { link = it },
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Label(
                    label = stringResource(ProfileCardRes.string.image),
                    isOptional = true,
                )

                image?.let {
                    Box(modifier = Modifier.size(120.dp)) {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(2.dp))
                                .align(Alignment.BottomStart),
                        )
                        IconButton(
                            onClick = { imageByteArray = null },
                            modifier = Modifier
                                .graphicsLayer {
                                    translationX = 12.dp.toPx()
                                    translationY = -12.dp.toPx()
                                }
                                .size(24.dp)
                                .align(Alignment.TopEnd),
                            colors = IconButtonDefaults
                                .iconButtonColors()
                                .copy(containerColor = Color(0xFF414849)),
                        ) {
                            Icon(Icons.Default.Close, null)
                        }
                    }
                } ?: run {
                    PhotoPickerButton(
                        onSelectedImage = { imageByteArray = it },
                        modifier = Modifier.testTag(ProfileCardSelectImageButtonTestTag),
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(ProfileCardRes.string.add_image))
                    }
                }
            }

            Text(stringResource(ProfileCardRes.string.select_theme))

            ThemePiker(selectedTheme = selectedTheme, onClickImage = { selectedTheme = it })

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
                modifier = Modifier.fillMaxWidth()
                    .testTag(ProfileCardCreateButtonTestTag),
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(ProfileCardRes.string.create_card),
                )
            }
        }
    }
}

@Composable
internal fun InputColumn(
    label: String,
    value: String,
    testTag: String,
    modifier: Modifier = Modifier,
    isOptional: Boolean = true,
    onValueChanged: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Label(
            label = label,
            isOptional = isOptional,
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier.fillMaxWidth().testTag(testTag),
        )
    }
}

@Composable
internal fun Label(
    label: String,
    isOptional: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(weight = 1.0f, fill = false),
        )
        if (isOptional) OptionLabel()
    }
}

@Composable
internal fun OptionLabel() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(vertical = 2.dp, horizontal = 6.dp),
    ) {
        Text(
            text = stringResource(ProfileCardRes.string.optional_input),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
internal fun ThemePiker(selectedTheme: ProfileCardTheme, onClickImage: (ProfileCardTheme) -> Unit) {
    val themes = ProfileCardTheme.entries.chunked(2)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        themes.forEach {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                it.firstOrNull()?.let {
                    ThemeImage(
                        modifier = Modifier.weight(1.0f),
                        isSelected = selectedTheme == it,
                        onClickImage = onClickImage,
                        theme = it,
                    )
                }
                it.getOrNull(1)?.let {
                    ThemeImage(
                        modifier = Modifier.weight(1.0f),
                        isSelected = selectedTheme == it,
                        onClickImage = onClickImage,
                        theme = it,
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeImage(
    isSelected: Boolean,
    theme: ProfileCardTheme,
    modifier: Modifier = Modifier,
    onClickImage: (ProfileCardTheme) -> Unit,
) {
    val colorMap = buildMap {
        put(ProfileCardTheme.Iguana, Color(0xFFB4FF79))
        put(ProfileCardTheme.Hedgehog, Color(0xFFFEB258))
        put(ProfileCardTheme.Giraffe, Color(0xFFFCF65F))
        put(ProfileCardTheme.Flamingo, Color(0xFFFF8EBD))
        put(ProfileCardTheme.Jellyfish, Color(0xFF6FD7F8))
        put(ProfileCardTheme.None, Color.White)
    }
    val selectedBorderColor = MaterialTheme.colorScheme.surfaceTint
    val painter = rememberVectorPainter(Icons.Default.Check)

    Image(
        painter = painterResource(ProfileCardRes.drawable.theme),
        contentDescription = null,
        modifier = modifier
            .selectedBorder(isSelected, selectedBorderColor, painter)
            .clip(RoundedCornerShape(2.dp))
            .background(colorMap[theme]!!)
            .clickable { onClickImage(theme) }
            .padding(top = 36.dp, start = 30.dp, end = 30.dp, bottom = 36.dp),
    )
}

fun Modifier.selectedBorder(
    isSelected: Boolean,
    selectedBorderColor: Color,
    vectorPainter: VectorPainter,
): Modifier = if (isSelected) {
    drawWithContent {
        drawRoundRect(
            color = selectedBorderColor,
            size = size,
            cornerRadius = CornerRadius(2.dp.toPx()),
            style = Stroke(8.dp.toPx(), cap = StrokeCap.Round),
        )
        drawContent()
        drawPath(
            color = selectedBorderColor,
            path = Path().apply {
                moveTo(size.width, 0f)
                lineTo(size.width - 44.dp.toPx(), 0f)
                lineTo(size.width, 44.dp.toPx())
            },
        )
        drawCircle(
            color = Color.White,
            center = Offset(size.width - 12.dp.toPx(), 13.dp.toPx()),
            radius = 10.dp.toPx(),
        )
        translate(left = size.width - 20.dp.toPx(), top = 5.dp.toPx()) {
            with(vectorPainter) {
                draw(size = Size(16.dp.toPx(), 16.dp.toPx()))
            }
        }
    }
} else {
    this
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
