package io.github.droidkaigi.confsched.profilecard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardTheme
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Edit
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder

const val profileCardScreenRoute = "profilecard"

object ProfileCardTestTag {
    private const val suffix = "TestTag"
    private const val prefix = "ProfileCard"

    object EditScreen {
        private const val editScreenPrefix = "${prefix}_EditScreen"
        const val SCREEN = "${editScreenPrefix}_$suffix"
        const val NICKNAME_TEXT_FIELD = "${editScreenPrefix}_NicknameTextField_$suffix"
        const val OCCUPATION_TEXT_FIELD = "${editScreenPrefix}_OccupationTextField_$suffix"
        const val LINK_TEXT_FIELD = "${editScreenPrefix}_LinkTextField_$suffix"
        const val SELECT_IMAGE_BUTTON = "${editScreenPrefix}_SelectImageButton_$suffix"
        const val CREATE_BUTTON = "${editScreenPrefix}_CreateButton_$suffix"
    }

    object CardScreen {
        private const val cardScreenPrefix = "${prefix}_CardScreen"
        const val SCREEN = "${cardScreenPrefix}_$suffix"
    }
}

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
        val nickname: String,
        val occupation: String?,
        val link: String?,
        val imageUri: String?,
        val theme: ProfileCardTheme,
    ) : ProfileCardUiState {
        companion object {
            fun initial() = Edit(
                nickname = "",
                occupation = null,
                link = null,
                imageUri = null,
                theme = ProfileCardTheme.Iguana,
            )
        }
    }

    data class Card(
        val nickname: String,
        val occupation: String?,
        val link: String?,
        val imageUri: String?,
        val theme: ProfileCardTheme,
    ) : ProfileCardUiState
}

internal data class ProfileCardScreenUiState(
    val isLoading: Boolean,
    val contentUiState: ProfileCardUiState,
    val userMessageStateHolder: UserMessageStateHolder,
)

internal fun ProfileCard.toUiState() =
    ProfileCardUiState.Card(
        nickname = nickname,
        occupation = occupation,
        link = link,
        imageUri = imageUri,
        theme = theme,
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
    uiState: ProfileCardScreenUiState = profileCardScreenPresenter(eventEmitter),
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
        when (val contentUiState = uiState.contentUiState) {
            is ProfileCardUiState.Edit -> {
                EditScreen(
                    uiState = contentUiState,
                    onClickCreate = {
                        eventEmitter.tryEmit(EditScreenEvent.CreateProfileCard(it))
                    },
                    contentPadding = padding,
                )
            }

            is ProfileCardUiState.Card -> {
                CardScreen(
                    uiState = contentUiState,
                    onClickReset = {
                        eventEmitter.tryEmit(CardScreenEvent.Reset)
                    },
                    contentPadding = padding,
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
    uiState: Edit,
    onClickCreate: (ProfileCard) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    var nickname by remember { mutableStateOf(uiState.nickname) }
    var occupation by remember { mutableStateOf(uiState.occupation) }
    var link by remember { mutableStateOf(uiState.link) }
    var imageUri by remember { mutableStateOf(uiState.imageUri) }

    Column(
        modifier = modifier
            .testTag(ProfileCardTestTag.EditScreen.SCREEN)
            .padding(contentPadding),
    ) {
        Text("ProfileCardEdit")
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            placeholder = { Text("Nickname") },
            modifier = Modifier.testTag(ProfileCardTestTag.EditScreen.NICKNAME_TEXT_FIELD),
        )
        TextField(
            value = occupation ?: "",
            onValueChange = { occupation = it },
            placeholder = { Text("Occupation") },
            modifier = Modifier.testTag(ProfileCardTestTag.EditScreen.OCCUPATION_TEXT_FIELD),
        )
        TextField(
            value = link ?: "",
            onValueChange = { link = it },
            placeholder = { Text("Link") },
            modifier = Modifier.testTag(ProfileCardTestTag.EditScreen.LINK_TEXT_FIELD),
        )
        Button(
            onClick = {},
            modifier = Modifier.testTag(ProfileCardTestTag.EditScreen.SELECT_IMAGE_BUTTON),
        ) {
            Text("画像を選択")
        }
        Button(
            onClick = {
                onClickCreate(
                    ProfileCard(
                        nickname = nickname,
                        occupation = occupation,
                        link = link,
                        imageUri = imageUri,
                        theme = uiState.theme,
                    ),
                )
            },
            modifier = Modifier.testTag(ProfileCardTestTag.EditScreen.CREATE_BUTTON),
        ) {
            Text("Create")
        }
    }
}

@Composable
internal fun CardScreen(
    uiState: ProfileCardUiState.Card,
    onClickReset: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .testTag(ProfileCardTestTag.CardScreen.SCREEN)
            .padding(contentPadding),
    ) {
        Text("ProfileCard")
        Text(uiState.nickname)
        if (uiState.occupation != null) {
            Text(uiState.occupation)
        }
        if (uiState.link != null) {
            Text(uiState.link)
        }
        Button(onClickReset) {
            Text("Reset")
        }
    }
}
