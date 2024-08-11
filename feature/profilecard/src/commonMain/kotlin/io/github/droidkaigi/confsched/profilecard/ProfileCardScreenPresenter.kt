package io.github.droidkaigi.confsched.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

internal sealed interface ProfileCardScreenEvent

internal sealed interface EditScreenEvent : ProfileCardScreenEvent {
    data object SelectImage : EditScreenEvent
    data class CreateProfileCard(val profileCard: ProfileCard) : EditScreenEvent
}

internal sealed interface CardScreenEvent : ProfileCardScreenEvent {
    data object ShareProfileCard : CardScreenEvent
    data object Reset : CardScreenEvent
}

@Composable
internal fun profileCardScreenPresenter(
    events: Flow<ProfileCardScreenEvent>,
): ProfileCardScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    // TODO: get from repository
    val profileCard: ProfileCard? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(false) }
    var contentUiState by remember {
        mutableStateOf(
            profileCard?.toUiState() ?: ProfileCardUiState.Edit.initial(),
        )
    }

    SafeLaunchedEffect(Unit) {
        events.collect {
            isLoading = true
            when (it) {
                CardScreenEvent.Reset -> {
                    userMessageStateHolder.showMessage("Reset")
                    contentUiState = ProfileCardUiState.Edit.initial()
                }

                CardScreenEvent.ShareProfileCard -> {
                    userMessageStateHolder.showMessage("Share Profile Card")
                }

                is EditScreenEvent.CreateProfileCard -> {
                    userMessageStateHolder.showMessage("Create Profile Card")
                    // TODO: save model by repository
                    contentUiState = it.profileCard.toUiState()
                }

                EditScreenEvent.SelectImage -> {
                    userMessageStateHolder.showMessage("Select Image")
                }
            }
            isLoading = false
        }
    }

    ProfileCardScreenUiState(
        isLoading = isLoading,
        contentUiState = contentUiState,
        userMessageStateHolder = userMessageStateHolder,
    )
}
