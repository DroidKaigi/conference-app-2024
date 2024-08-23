package io.github.droidkaigi.confsched.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import conference_app_2024.feature.profilecard.generated.resources.add_validate_format
import conference_app_2024.feature.profilecard.generated.resources.enter_validate_format
import conference_app_2024.feature.profilecard.generated.resources.image
import conference_app_2024.feature.profilecard.generated.resources.link
import conference_app_2024.feature.profilecard.generated.resources.nickname
import conference_app_2024.feature.profilecard.generated.resources.occupation
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardRepository
import io.github.droidkaigi.confsched.model.localProfileCardRepository
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiType.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

internal sealed interface ProfileCardScreenEvent

internal sealed interface EditScreenEvent : ProfileCardScreenEvent {
    data class OnChangeNickname(
        val nickname: String,
    ) : EditScreenEvent

    data class OnChangeOccupation(
        val occupation: String,
    ) : EditScreenEvent

    data class OnChangeLink(
        val link: String,
    ) : EditScreenEvent

    data class OnChangeImage(
        val image: String,
    ) : EditScreenEvent

    data object SelectImage : EditScreenEvent
    data class Create(val profileCard: ProfileCard.Exists) : EditScreenEvent
}

internal sealed interface CardScreenEvent : ProfileCardScreenEvent {
    data object Edit : CardScreenEvent
}

private fun ProfileCard.toEditUiState(): ProfileCardUiState.Edit {
    return when (this) {
        is ProfileCard.Exists -> ProfileCardUiState.Edit(
            nickname = nickname,
            occupation = occupation,
            link = link,
            image = image,
            cardType = cardType,
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
            cardType = cardType,
        )

        ProfileCard.DoesNotExists, ProfileCard.Loading -> null
    }
}

@Composable
internal fun profileCardScreenPresenter(
    events: Flow<ProfileCardScreenEvent>,
    repository: ProfileCardRepository = localProfileCardRepository(),
): ProfileCardScreenState = providePresenterDefaults { userMessageStateHolder ->
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

    val profileCard: ProfileCard by rememberUpdatedState(repository.profileCard())
    var isLoading: Boolean by remember { mutableStateOf(false) }
    val editUiState: ProfileCardUiState.Edit by rememberUpdatedState(profileCard.toEditUiState())
    val cardUiState: ProfileCardUiState.Card? by rememberUpdatedState(profileCard.toCardUiState())
    var cardError by remember { mutableStateOf(ProfileCardError()) }
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
        events.collect { event ->
            when (event) {
                is CardScreenEvent.Edit -> {
                    isLoading = true
                    launch {
                        userMessageStateHolder.showMessage("Edit")
                    }
                    uiType = ProfileCardUiType.Edit
                    isLoading = false
                }

                is EditScreenEvent.Create -> {
                    isLoading = true
                    launch {
                        userMessageStateHolder.showMessage("Create Profile Card")
                    }
                    repository.save(event.profileCard)
                    uiType = Card
                    isLoading = false
                }

                is EditScreenEvent.SelectImage -> {
                    isLoading = true
                    launch {
                        userMessageStateHolder.showMessage("Select Image")
                    }
                    isLoading = false
                }

                is EditScreenEvent.OnChangeNickname -> {
                    cardError = cardError.copy(
                        nicknameError = if (event.nickname.isEmpty()) nicknameValidationErrorString else "",
                    )
                }

                is EditScreenEvent.OnChangeOccupation -> {
                    cardError = cardError.copy(
                        occupationError = if (event.occupation.isEmpty()) occupationValidationErrorString else "",
                    )
                }

                is EditScreenEvent.OnChangeLink -> {
                    cardError = cardError.copy(
                        linkError = if (event.link.isEmpty()) linkValidationErrorString else "",
                    )
                }

                is EditScreenEvent.OnChangeImage -> {
                    cardError = cardError.copy(
                        imageError = if (event.image.isEmpty()) imageValidationErrorString else "",
                    )
                }
            }
        }
    }

    ProfileCardScreenState(
        isLoading = isLoading,
        editUiState = editUiState,
        cardUiState = cardUiState,
        cardError = cardError,
        uiType = uiType,
        userMessageStateHolder = userMessageStateHolder,
    )
}
