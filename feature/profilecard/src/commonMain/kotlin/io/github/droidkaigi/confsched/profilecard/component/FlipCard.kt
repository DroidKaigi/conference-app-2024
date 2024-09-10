package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.profilecard.generated.resources.flip
import io.github.droidkaigi.confsched.droidkaigiui.WithDeviceOrientation
import io.github.droidkaigi.confsched.profilecard.ProfileCardRes
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import io.github.droidkaigi.confsched.profilecard.hologramaticEffect
import io.github.droidkaigi.confsched.profilecard.tiltEffect
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

const val ProfileCardFlipCardTestTag = "ProfileCardFlipCardTestTag"

private const val ChangeFlipCardDeltaThreshold = 20f

@Composable
internal fun FlipCard(
    uiState: Card,
    profileImagePainter: Painter,
    qrCodeImagePainter: Painter,
    modifier: Modifier = Modifier,
    isCreated: Boolean = false,
) {
    var initialRotation = rememberAnimatedInitialRotation(isCreated)
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else initialRotation,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )
    val isBack by remember { derivedStateOf { rotation > 90f } }

    WithDeviceOrientation {
        Card(
            modifier = modifier
                .testTag(ProfileCardFlipCardTestTag)
                .size(width = 300.dp, height = 380.dp)
                .clickable(onClickLabel = stringResource(ProfileCardRes.string.flip)) {
                    isFlipped = isFlipped.not()
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (isFlipped && delta > ChangeFlipCardDeltaThreshold) {
                            isFlipped = false
                        }
                        if (isFlipped.not() && delta < -ChangeFlipCardDeltaThreshold) {
                            isFlipped = true
                        }
                    },
                )
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .tiltEffect(this@WithDeviceOrientation),
            elevation = CardDefaults.cardElevation(10.dp),
        ) {
            if (isBack) { // Back
                FlipCardBack(uiState, qrCodeImagePainter)
            } else { // Front
                FlipCardFront(
                    modifier = Modifier.hologramaticEffect(this@WithDeviceOrientation),
                    uiState = uiState,
                    profileImagePainter = profileImagePainter,
                )
            }
        }
    }
}

@Composable
private fun rememberAnimatedInitialRotation(isCreated: Boolean): Float {
    var isCreated by rememberSaveable { mutableStateOf(isCreated) }
    var initialRotation by remember { mutableStateOf(0f) }
    val targetRotation by animateFloatAsState(
        targetValue = 30f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )
    val targetRotation2 by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )

    LaunchedEffect(Unit) {
        if (isCreated) {
            initialRotation = targetRotation
            delay(400)
            initialRotation = targetRotation2
            isCreated = false
        }
    }
    return initialRotation
}
