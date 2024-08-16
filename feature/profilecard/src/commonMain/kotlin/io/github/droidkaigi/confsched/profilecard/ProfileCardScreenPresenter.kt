package io.github.droidkaigi.confsched.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.ImageData
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardRepository
import io.github.droidkaigi.confsched.model.localProfileCardRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.flow.Flow

internal sealed interface ProfileCardScreenEvent

internal sealed interface EditScreenEvent : ProfileCardScreenEvent {
    data object SelectImage : EditScreenEvent
    data class Create(val profileCard: ProfileCard.Exists) : EditScreenEvent
}

internal sealed interface CardScreenEvent : ProfileCardScreenEvent {
    data object Share : CardScreenEvent
    data object Edit : CardScreenEvent
}

private fun ProfileCard.toEditUiState(): ProfileCardUiState.Edit {
    return when (this) {
        is ProfileCard.Exists -> ProfileCardUiState.Edit(
            nickname = nickname,
            occupation = occupation,
            link = link,
            imageData = image?.run(::ImageData),
            theme = theme,
        )
        ProfileCard.DoesNotExists, ProfileCard.Loading -> ProfileCardUiState.Edit()
    }
}

private fun ProfileCard.toCardUiState(): ProfileCardUiState.Card? {
    return when (this) {
        is ProfileCard.Exists -> ProfileCardUiState.Card(
            nickname = nickname,
            occupation = occupation,
            link = link,
            image = image,
            theme = theme,
        )
        ProfileCard.DoesNotExists, ProfileCard.Loading -> null
    }
}

@Composable
internal fun profileCardScreenPresenter(
    events: Flow<ProfileCardScreenEvent>,
    repository: ProfileCardRepository = localProfileCardRepository(),
): ProfileCardScreenState = providePresenterDefaults { userMessageStateHolder ->
    val profileCard: ProfileCard by rememberUpdatedState(repository.profileCard())
    var isLoading: Boolean by remember { mutableStateOf(false) }
    var editingUiState: ProfileCardUiState.Edit? by rememberRetained { mutableStateOf(null) }
    val editUiState: ProfileCardUiState.Edit by rememberUpdatedState(editingUiState ?: profileCard.toEditUiState())
    val cardUiState: ProfileCardUiState.Card? by rememberUpdatedState(profileCard.toCardUiState())
    var uiType: ProfileCardUiType by remember { mutableStateOf(ProfileCardUiType.Loading) }

    // at first launch, if you have a profile card, show card ui
    SafeLaunchedEffect(profileCard) {
        uiType = when (profileCard) {
            is ProfileCard.Exists -> ProfileCardUiType.Card
            ProfileCard.DoesNotExists -> ProfileCardUiType.Edit
            ProfileCard.Loading -> ProfileCardUiType.Loading
        }
    }

    SafeLaunchedEffect(Unit) {
        events.collect {
            isLoading = true
            when (it) {
                CardScreenEvent.Edit -> {
                    userMessageStateHolder.showMessage("Edit")
                    uiType = ProfileCardUiType.Edit
                }

                CardScreenEvent.Share -> {
                    userMessageStateHolder.showMessage("Share Profile Card")
                }

                is EditScreenEvent.Create -> {
                    userMessageStateHolder.showMessage("Create Profile Card")
                    repository.save(it.profileCard)
                    uiType = ProfileCardUiType.Card
                }

                EditScreenEvent.SelectImage -> {
                    userMessageStateHolder.showMessage("Select Image")
                }
            }
            isLoading = false
        }
    }

    ProfileCardScreenState(
        isLoading = isLoading,
        editUiState = editUiState,
        cardUiState = cardUiState,
        uiType = uiType,
        userMessageStateHolder = userMessageStateHolder,
    )
}
