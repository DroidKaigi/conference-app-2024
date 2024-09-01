package io.github.droidkaigi.confsched.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import conference_app_2024.feature.profilecard.generated.resources.add_validate_format
import conference_app_2024.feature.profilecard.generated.resources.droidkaigi_logo
import conference_app_2024.feature.profilecard.generated.resources.enter_validate_format
import conference_app_2024.feature.profilecard.generated.resources.image
import conference_app_2024.feature.profilecard.generated.resources.link
import conference_app_2024.feature.profilecard.generated.resources.nickname
import conference_app_2024.feature.profilecard.generated.resources.occupation
import conference_app_2024.feature.profilecard.generated.resources.url_is_invalid
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardRepository
import io.github.droidkaigi.confsched.model.localProfileCardRepository
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

sealed interface ProfileCardScreenEvent

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

internal fun ProfileCard.toEditUiState(): ProfileCardUiState.Edit {
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

internal fun ProfileCard.toCardUiState(): ProfileCardUiState.Card? {
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

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun profileCardScreenPresenter(
    events: EventFlow<ProfileCardScreenEvent>,
    repository: ProfileCardRepository = localProfileCardRepository(),
): ProfileCardScreenState = providePresenterDefaults { userMessageStateHolder ->
    val emptyNicknameErrorString = stringResource(
        ProfileCardRes.string.enter_validate_format,
        stringResource(ProfileCardRes.string.nickname),
    )
    val emptyOccupationErrorString = stringResource(
        ProfileCardRes.string.enter_validate_format,
        stringResource(ProfileCardRes.string.occupation),
    )
    val emptyLinkErrorString = stringResource(
        ProfileCardRes.string.enter_validate_format,
        stringResource(ProfileCardRes.string.link),
    )
    val invalidLinkErrorString = stringResource(
        ProfileCardRes.string.url_is_invalid,
    )
    val emptyImageErrorString = stringResource(
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

    var qrCodeImageByteArray by remember { mutableStateOf(ByteArray(0)) }
    val link = cardUiState?.link
    SafeLaunchedEffect(link) {
        link?.let { link ->
            qrCodeImageByteArray = repository
                .loadQrCodeImageByteArray(
                    link = link,
                    centerLogoRes = ProfileCardRes.drawable.droidkaigi_logo,
                )
        }
    }

    EventEffect(events) { event ->
        when (event) {
            is CardScreenEvent.Edit -> {
                uiType = ProfileCardUiType.Edit
            }

            is EditScreenEvent.Create -> {
                isLoading = true
                repository.save(event.profileCard)
                uiType = ProfileCardUiType.Card
                isLoading = false
            }

            is EditScreenEvent.SelectImage -> {
                // NOOP Put in some processing if necessary.
            }

            is EditScreenEvent.OnChangeNickname -> {
                cardError = cardError.copy(
                    nicknameError = if (event.nickname.isEmpty()) emptyNicknameErrorString else "",
                )
            }

            is EditScreenEvent.OnChangeOccupation -> {
                cardError = cardError.copy(
                    occupationError = if (event.occupation.isEmpty()) emptyOccupationErrorString else "",
                )
            }

            is EditScreenEvent.OnChangeLink -> {
                // Only matches if the link is in this format "${http or https + ://}${sub domain + .}${domain}.${tld}/${sub directories}".
                // Protocol, sub domain and sub directories are optional.
                // ex. https://www.example.com/hogefuga/foobar
                val invalidFormat = event.link.matches(Regex("^(?:https?://)?(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9-]{2,}(?:/\\S*)?\$")).not()
                cardError = cardError.copy(
                    linkError = if (event.link.isEmpty()) emptyLinkErrorString else if (invalidFormat) invalidLinkErrorString else "",
                )
            }

            is EditScreenEvent.OnChangeImage -> {
                cardError = cardError.copy(
                    imageError = if (event.image.isEmpty()) emptyImageErrorString else "",
                )
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
        qrCodeImageByteArray = qrCodeImageByteArray,
    )
}
