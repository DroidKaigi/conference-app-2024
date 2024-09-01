package io.github.droidkaigi.confsched.profilecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntRect
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.ProfileCardRepository
import io.github.droidkaigi.confsched.model.ProfileImage
import io.github.droidkaigi.confsched.model.localProfileCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal sealed interface CropImageScreenEvent {
    data class Crop(val rect: IntRect) : CropImageScreenEvent
    data object Confirm : CropImageScreenEvent
    data object Cancel : CropImageScreenEvent
}

@Composable
internal fun cropImageScreenPresenter(
    onConfirm: () -> Unit,
    events: Flow<CropImageScreenEvent>,
    repository: ProfileCardRepository = localProfileCardRepository(),
): CropImageScreenState = providePresenterDefaults { _ ->
    val profileImageCandidate: ProfileImage? by rememberUpdatedState(repository.profileImageCandidate())
    var croppedProfileImage: ProfileImage? by remember { mutableStateOf(null) }
    var isProcessing: Boolean by remember { mutableStateOf(false) }

    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is CropImageScreenEvent.Crop -> {
                    isProcessing = true

                    withContext(Dispatchers.Default) {
                        croppedProfileImage = requireNotNull(profileImageCandidate).crop(event.rect)
                    }

                    isProcessing = false
                }

                is CropImageScreenEvent.Confirm -> {
                    val result = requireNotNull(croppedProfileImage)
                    repository.setProfileImageInEdit(result)

                    onConfirm()
                }

                is CropImageScreenEvent.Cancel -> {
                    croppedProfileImage = null
                }
            }
        }
    }

    val candidate = profileImageCandidate
    val cropped = croppedProfileImage
    when {
        candidate == null -> {
            CropImageScreenState.Init
        }

        cropped == null -> {
            CropImageScreenState.Select(
                profileImage = candidate,
                isProcessing = isProcessing,
            )
        }

        else -> {
            CropImageScreenState.Confirm(
                profileImage = cropped,
            )
        }
    }
}
