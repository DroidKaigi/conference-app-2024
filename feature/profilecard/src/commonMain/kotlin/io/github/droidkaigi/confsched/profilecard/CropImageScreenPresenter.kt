package io.github.droidkaigi.confsched.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.IntRect
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.ProfileCardRepository
import io.github.droidkaigi.confsched.model.ProfileImage
import io.github.droidkaigi.confsched.model.localProfileCardRepository
import io.github.droidkaigi.confsched.profilecard.CropImageScreenEvent.Crop
import kotlinx.coroutines.flow.Flow

internal sealed interface CropImageScreenEvent {
    data class Crop(val rect: IntRect) : CropImageScreenEvent
}

@Composable
internal fun cropImageScreenPresenter(
    events: Flow<CropImageScreenEvent>,
    repository: ProfileCardRepository = localProfileCardRepository(),
): CropImageScreenState = providePresenterDefaults { _ ->
    val profileImage: ProfileImage? by rememberUpdatedState(repository.profileImageCandidate())

    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Crop -> {
                    // TODO
                }
            }
        }
    }
    CropImageScreenState(
        profileImage = profileImage,
    )
}
