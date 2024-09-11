package io.github.droidkaigi.confsched.profilecard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import co.touchlab.kermit.Logger
import coil3.compose.rememberAsyncImagePainter
import com.preat.peekaboo.image.picker.toImageBitmap
import conference_app_2024.feature.profilecard.generated.resources.add_image
import conference_app_2024.feature.profilecard.generated.resources.card_type
import conference_app_2024.feature.profilecard.generated.resources.create_card
import conference_app_2024.feature.profilecard.generated.resources.delete
import conference_app_2024.feature.profilecard.generated.resources.edit
import conference_app_2024.feature.profilecard.generated.resources.icon_share
import conference_app_2024.feature.profilecard.generated.resources.image
import conference_app_2024.feature.profilecard.generated.resources.link
import conference_app_2024.feature.profilecard.generated.resources.link_example_text
import conference_app_2024.feature.profilecard.generated.resources.nickname
import conference_app_2024.feature.profilecard.generated.resources.occupation
import conference_app_2024.feature.profilecard.generated.resources.profile_card_edit_description
import conference_app_2024.feature.profilecard.generated.resources.profile_card_title
import conference_app_2024.feature.profilecard.generated.resources.select_theme
import conference_app_2024.feature.profilecard.generated.resources.share
import conference_app_2024.feature.profilecard.generated.resources.share_description
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProfileCardTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardTheme
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.component.resetScroll
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardType
import io.github.droidkaigi.confsched.profilecard.component.FlipCard
import io.github.droidkaigi.confsched.profilecard.component.InvertSystemBarAppearance
import io.github.droidkaigi.confsched.profilecard.component.PhotoPickerButton
import io.github.droidkaigi.confsched.profilecard.component.ShareableCard
import io.ktor.util.decodeBase64Bytes
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val profileCardScreenRoute = "profilecard"

const val ProfileCardEditScreenColumnTestTag = "ProfileCardEditScreenColumnTestTag"
const val ProfileCardNicknameTextFieldTestTag = "ProfileCardNicknameTextFieldTestTag"
const val ProfileCardOccupationTextFieldTestTag = "ProfileCardOccupationTextFieldTestTag"
const val ProfileCardLinkTextFieldTestTag = "ProfileCardLinkTextFieldTestTag"
const val ProfileCardInputErrorTextTestTag = "ProfileCardInputErrorTextTestTag"
const val ProfileCardSelectImageButtonTestTag = "ProfileCardSelectImageButtonTestTag"
const val ProfileCardCreateButtonTestTag = "ProfileCardCreateButtonTestTag"
const val ProfileCardCardScreenTestTag = "ProfileCardCardScreenTestTag"
const val ProfileCardEditButtonTestTag = "ProfileCardEditButtonTestTag"
const val ProfileCardShareButtonTestTag = "ProfileCardShareButtonTestTag"

fun NavGraphBuilder.profileCardScreen(
    contentPadding: PaddingValues,
    onClickShareProfileCard: (String, ImageBitmap) -> Unit,
) {
    composable(profileCardScreenRoute) {
        ProfileCardScreen(
            contentPadding = contentPadding,
            onClickShareProfileCard = onClickShareProfileCard,
        )
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

sealed interface ProfileCardUiState {
    data class Edit(
        val nickname: String = "",
        val occupation: String = "",
        val link: String = "",
        val image: String? = null,
        val cardType: ProfileCardType = ProfileCardType.Iguana,
    ) : ProfileCardUiState

    data class Card(
        val nickname: String,
        val occupation: String,
        val link: String,
        val image: String,
        val cardType: ProfileCardType,
    ) : ProfileCardUiState
}

data class ProfileCardError(
    val nicknameError: String = "",
    val occupationError: String = "",
    val linkError: String = "",
    val imageError: String = "",
)

enum class ProfileCardUiType {
    Loading,
    Edit,
    Card,
}

data class ProfileCardScreenState(
    val isLoading: Boolean,
    val editUiState: ProfileCardUiState.Edit,
    val cardUiState: ProfileCardUiState.Card?,
    val cardError: ProfileCardError,
    val uiType: ProfileCardUiType,
    val userMessageStateHolder: UserMessageStateHolder,
    val qrCodeImageByteArray: ByteArray? = null,
)

@Composable
fun ProfileCardScreen(
    onClickShareProfileCard: (String, ImageBitmap) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    ProfileCardScreen(
        contentPadding = contentPadding,
        onClickShareProfileCard = onClickShareProfileCard,
        modifier = modifier,
        eventFlow = rememberEventFlow(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileCardScreen(
    contentPadding: PaddingValues,
    onClickShareProfileCard: (String, ImageBitmap) -> Unit,
    modifier: Modifier = Modifier,
    eventFlow: EventFlow<ProfileCardScreenEvent> = rememberEventFlow(),
    uiState: ProfileCardScreenState = profileCardScreenPresenter(eventFlow),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val layoutDirection = LocalLayoutDirection.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    LaunchedEffect(uiState.uiType) {
        if (
            uiState.uiType == ProfileCardUiType.Card ||
            uiState.uiType == ProfileCardUiType.Edit
        ) {
            scrollBehavior.resetScroll()
        }
    }

    val contentWindowInsetsBottom = max(
        contentPadding.calculateBottomPadding(),
        with(LocalDensity.current) { WindowInsets.ime.getBottom(this).toDp() },
    )
    Scaffold(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(
            left = contentPadding.calculateLeftPadding(layoutDirection),
            top = contentPadding.calculateTopPadding(),
            right = contentPadding.calculateRightPadding(layoutDirection),
            bottom = contentWindowInsetsBottom
                .plus(16.dp), // Adjusting Snackbar position
        ),
        topBar = {
            when (uiState.uiType) {
                ProfileCardUiType.Loading -> {
                    // NOOP
                }

                ProfileCardUiType.Edit -> {
                    AnimatedTextTopAppBar(
                        title = stringResource(ProfileCardRes.string.profile_card_title),
                        scrollBehavior = scrollBehavior,
                    )
                }

                ProfileCardUiType.Card -> {
                    if (uiState.cardUiState == null) return@Scaffold
                    ProvideProfileCardTheme(uiState.cardUiState.cardType.toString()) {
                        AnimatedTextTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = LocalProfileCardTheme.current.primaryColor,
                                scrolledContainerColor = LocalProfileCardTheme.current.primaryColor,
                            ),
                            textColor = MaterialTheme.colorScheme.scrim,
                            title = stringResource(ProfileCardRes.string.profile_card_title),
                            scrollBehavior = scrollBehavior,
                        )
                    }
                }
            }
        },
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
                    profileCardError = uiState.cardError,
                    scrollBehavior = scrollBehavior,
                    onChangeNickname = {
                        eventFlow.tryEmit(EditScreenEvent.OnChangeNickname(it))
                    },
                    onChangeOccupation = {
                        eventFlow.tryEmit(EditScreenEvent.OnChangeOccupation(it))
                    },
                    onChangeLink = {
                        eventFlow.tryEmit(EditScreenEvent.OnChangeLink(it))
                    },
                    onChangeImage = {
                        eventFlow.tryEmit(EditScreenEvent.OnChangeImage(it))
                    },
                    onClickCreate = {
                        eventFlow.tryEmit(EditScreenEvent.Create(it))
                    },
                    contentPadding = padding,
                )
            }

            ProfileCardUiType.Card -> {
                if (uiState.cardUiState == null) return@Scaffold
                val shareText = stringResource(ProfileCardRes.string.share_description)

                CardScreen(
                    uiState = uiState.cardUiState,
                    scrollBehavior = scrollBehavior,
                    onClickEdit = {
                        eventFlow.tryEmit(CardScreenEvent.Edit)
                    },
                    onClickShareProfileCard = { imageBitmap ->
                        onClickShareProfileCard(shareText, imageBitmap)
                    },
                    contentPadding = padding,
                    isCreated = true,
                    qrCodeImageByte = uiState.qrCodeImageByteArray,
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
    profileCardError: ProfileCardError,
    scrollBehavior: TopAppBarScrollBehavior,
    onChangeNickname: (String) -> Unit,
    onChangeOccupation: (String) -> Unit,
    onChangeLink: (String) -> Unit,
    onChangeImage: (String) -> Unit,
    onClickCreate: (ProfileCard.Exists) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    var nickname by rememberSaveable { mutableStateOf(uiState.nickname) }
    var occupation by rememberSaveable { mutableStateOf(uiState.occupation) }
    var link by rememberSaveable { mutableStateOf(uiState.link) }
    var imageByteArray: ByteArray? by remember { mutableStateOf(uiState.image?.decodeBase64Bytes()) }
    val image by remember { derivedStateOf { imageByteArray?.toImageBitmap() } }
    var selectedCardType by rememberSaveable { mutableStateOf(uiState.cardType) }

    val isValidInputs by remember {
        derivedStateOf {
            nickname.isNotEmpty() && occupation.isNotEmpty() && link.isNotEmpty() && image != null
        }
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 16.dp)
            .testTag(ProfileCardEditScreenColumnTestTag),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(stringResource(ProfileCardRes.string.profile_card_edit_description))

        InputFieldWithError(
            value = nickname,
            labelString = stringResource(ProfileCardRes.string.nickname),
            errorMessage = profileCardError.nicknameError,
            textFieldTestTag = ProfileCardNicknameTextFieldTestTag,
            onValueChange = {
                nickname = it
                onChangeNickname(it)
            },
            onInputClear = {
                nickname = ""
                onChangeNickname("")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        InputFieldWithError(
            value = occupation,
            labelString = stringResource(ProfileCardRes.string.occupation),
            errorMessage = profileCardError.occupationError,
            textFieldTestTag = ProfileCardOccupationTextFieldTestTag,
            onValueChange = {
                occupation = it
                onChangeOccupation(it)
            },
            onInputClear = {
                occupation = ""
                onChangeOccupation("")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        val linkLabel = stringResource(ProfileCardRes.string.link)
            .plus(stringResource(ProfileCardRes.string.link_example_text))
        InputFieldWithError(
            value = link,
            labelString = linkLabel,
            errorMessage = profileCardError.linkError,
            textFieldTestTag = ProfileCardLinkTextFieldTestTag,
            onValueChange = {
                link = it
                onChangeLink(it)
            },
            onInputClear = {
                link = ""
                onChangeLink("")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Uri,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                },
            ),
        )

        Column(
            modifier = Modifier.padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column {
                Label(label = stringResource(ProfileCardRes.string.image))
                Spacer(modifier = Modifier.height(12.dp))
                ImagePickerWithError(
                    image = image,
                    onSelectedImage = {
                        imageByteArray = it
                        onChangeImage(it.toBase64())
                    },
                    errorMessage = profileCardError.imageError,
                    onClearImage = {
                        imageByteArray = null
                        onChangeImage("")
                    },
                )
            }

            Text(
                text = stringResource(ProfileCardRes.string.select_theme),
                style = MaterialTheme.typography.titleMedium,
            )

            CardTypePiker(
                selectedCardType = selectedCardType,
                onClickImage = { selectedCardType = it },
            )

            Button(
                onClick = {
                    onClickCreate(
                        ProfileCard.Exists(
                            nickname = nickname,
                            occupation = occupation,
                            link = link,
                            image = imageByteArray?.toBase64() ?: "",
                            cardType = selectedCardType,
                        ),
                    )
                },
                enabled = isValidInputs,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 12.dp)
                    .testTag(ProfileCardCreateButtonTestTag),
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(ProfileCardRes.string.create_card),
                )
            }
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
internal fun ByteArray.toBase64(): String = Base64.encode(this)

@OptIn(ExperimentalEncodingApi::class)
internal fun String.decodeBase64Bytes(): ByteArray = Base64.decode(this)

@Composable
internal fun Label(label: String) {
    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = label,
        style = MaterialTheme.typography.titleMedium,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputFieldWithError(
    value: String,
    labelString: String,
    errorMessage: String,
    textFieldTestTag: String,
    onValueChange: (String) -> Unit,
    onInputClear: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = 1,
    singleLine: Boolean = true,
) {
    Column(modifier = modifier) {
        val isError = errorMessage.isNotEmpty()
        val indicatorColor = if (isError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        }
        val interactionSource = remember { MutableInteractionSource() }

        Label(label = labelString)
        OutlinedTextField(
            value = value,
            textStyle = MaterialTheme.typography.bodyLarge,
            onValueChange = onValueChange,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = onInputClear,
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                bottom = 8.dp,
                                end = 4.dp,
                            ),
                    ) {
                        Icon(
                            modifier = Modifier.padding(8.dp),
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = stringResource(ProfileCardRes.string.delete),
                        )
                    }
                }
            },
            isError = isError,
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            singleLine = singleLine,
            modifier = Modifier
                .indicatorLine(
                    enabled = false,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = indicatorColor,
                        focusedContainerColor = indicatorColor,
                    ),
                    focusedIndicatorLineThickness = 3.dp,
                    unfocusedIndicatorLineThickness = 1.dp,
                )
                .fillMaxWidth()
                .background(Color.Transparent)
                .testTag(textFieldTestTag),
        )

        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 4.dp,
                    end = 16.dp,
                ).testTag(ProfileCardInputErrorTextTestTag.plus(value)),
        )
    }
}

@Composable
private fun ImagePickerWithError(
    image: ImageBitmap?,
    errorMessage: String,
    onSelectedImage: (ByteArray) -> Unit,
    onClearImage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        image?.let {
            Box(
                modifier = Modifier
                    .padding(bottom = 44.dp)
                    .size(120.dp),
            ) {
                Image(
                    bitmap = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(2.dp))
                        .align(Alignment.BottomStart),
                )
                IconButton(
                    onClick = onClearImage,
                    modifier = Modifier
                        .graphicsLayer {
                            translationX = 9.dp.toPx()
                            translationY = -9.dp.toPx()
                        }
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .size(24.dp)
                        .align(Alignment.TopEnd),
                    colors = IconButtonDefaults
                        .iconButtonColors()
                        .copy(containerColor = Color(0xFF414849)),
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(ProfileCardRes.string.delete),
                    )
                }
            }
        } ?: run {
            PhotoPickerButton(
                onSelectedImage = onSelectedImage,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .testTag(ProfileCardSelectImageButtonTestTag),
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(ProfileCardRes.string.add_image))
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
        }
    }
}

@Composable
internal fun CardTypePiker(
    selectedCardType: ProfileCardType,
    onClickImage: (ProfileCardType) -> Unit,
) {
    val cardTypes = ProfileCardType.entries.chunked(2)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        cardTypes.forEach {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                it.firstOrNull()?.let {
                    CardTypeImage(
                        modifier = Modifier.weight(1.0f),
                        isSelected = selectedCardType == it,
                        onClickImage = onClickImage,
                        cardType = it,
                    )
                }
                it.getOrNull(1)?.let {
                    CardTypeImage(
                        modifier = Modifier.weight(1.0f),
                        isSelected = selectedCardType == it,
                        onClickImage = onClickImage,
                        cardType = it,
                    )
                }
            }
        }
    }
}

@Composable
private fun CardTypeImage(
    isSelected: Boolean,
    cardType: ProfileCardType,
    modifier: Modifier = Modifier,
    onClickImage: (ProfileCardType) -> Unit,
) {
    val colorMap = ProfileCardType.entries.associateWith { type ->
        ProfileCardTheme.of(type.name).primaryColor
    }
    val selectedBorderColor = MaterialTheme.colorScheme.surfaceTint
    val painter = rememberVectorPainter(Icons.Default.Check)

    Image(
        painter = painterResource(ProfileCardRes.drawable.card_type),
        contentDescription = cardType.toString(),
        modifier = modifier
            .selectedBorder(isSelected, selectedBorderColor, painter)
            .clip(RoundedCornerShape(2.dp))
            .background(colorMap[cardType]!!)
            .selectable(isSelected) { onClickImage(cardType) }
            .padding(top = 36.dp, start = 30.dp, end = 30.dp, bottom = 36.dp),
    )
}

private fun Modifier.selectedBorder(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CardScreen(
    uiState: ProfileCardUiState.Card,
    onClickEdit: () -> Unit,
    onClickShareProfileCard: (ImageBitmap) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    qrCodeImageByte: ByteArray?,
    modifier: Modifier = Modifier,
    isCreated: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    var isShareReady by remember { mutableStateOf(false) }
    val profileCardImagePainter = rememberProfileImagePainter(uiState.image)
    val qrCodeImagePainter = rememberAsyncImagePainter(qrCodeImageByte)

    // The background of this screen is light, contrasting any other screen in the app.
    // Invert the content color of system bars to accommodate this unique property.
    InvertSystemBarAppearance()

    ProvideProfileCardTheme(uiState.cardType.toString()) {
        Box {
            // Not for display, for sharing
            ShareableCard(
                uiState = uiState,
                graphicsLayer = graphicsLayer,
                profileImagePainter = profileCardImagePainter,
                qrCodeImagePainter = qrCodeImagePainter,
                onReadyShare = {
                    Logger.d { "Ready to share" }
                    isShareReady = true
                },
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(LocalProfileCardTheme.current.primaryColor)
                    .testTag(ProfileCardCardScreenTestTag),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 32.dp)
                        .padding(contentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    FlipCard(
                        uiState = uiState,
                        profileImagePainter = profileCardImagePainter,
                        qrCodeImagePainter = qrCodeImagePainter,
                        isCreated = isCreated,
                    )
                    Spacer(Modifier.height(32.dp))
                    Logger.d { "isReadyShare: $isShareReady uiState.cardType:${uiState.cardType}" }
                    Button(
                        enabled = isShareReady,
                        onClick = {
                            coroutineScope.launch {
                                onClickShareProfileCard(graphicsLayer.toImageBitmap())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            disabledContainerColor = Color.White,
                        ),
                        border = if (uiState.cardType == ProfileCardType.None) {
                            BorderStroke(
                                0.5.dp,
                                Color.Black,
                            )
                        } else {
                            null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(ProfileCardShareButtonTestTag)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            painter = painterResource(ProfileCardRes.drawable.icon_share),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(ProfileCardRes.string.share),
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = {
                            onClickEdit()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .testTag(ProfileCardEditButtonTestTag),
                    ) {
                        Text(
                            text = stringResource(ProfileCardRes.string.edit),
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberProfileImagePainter(
    imageBase64String: String,
) = rememberAsyncImagePainter(
    model = rememberSaveable { imageBase64String.decodeBase64Bytes() },
)
