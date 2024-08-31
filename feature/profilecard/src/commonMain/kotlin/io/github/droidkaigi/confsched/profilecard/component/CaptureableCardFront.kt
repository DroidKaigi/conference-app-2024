package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImagePainter
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalClock
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import kotlinx.coroutines.flow.first

@Composable
internal fun BackgroundCapturableCardFront(
    uiState: Card,
    profileImagePainter: AsyncImagePainter,
    onCaptured: (ImageBitmap) -> Unit,
) {
    val clock = LocalClock.current
    val graphicsLayer = rememberGraphicsLayer()
    var lastCaptureTime by remember { mutableStateOf(0L) }

    LaunchedEffect(lastCaptureTime) {
        if (lastCaptureTime == 0L) {
            return@LaunchedEffect
        }
        profileImagePainter.state.first { it is AsyncImagePainter.State.Success }
        Logger.d { "BackgroundCapturableCardFront: onCaptured" }
        onCaptured(graphicsLayer.toImageBitmap())
    }

    Box(
        modifier = Modifier
            .drawWithCache {
                onDrawWithContent {
                    graphicsLayer.record {
                        this@onDrawWithContent.drawContent()
                    }
                    if (graphicsLayer.size.height > 0 && graphicsLayer.size.width > 0) {
                        lastCaptureTime = clock.now().toEpochMilliseconds()
                    }
                    drawLayer(graphicsLayer)
                }
            },
    ) {
        FlipCardFront(
            uiState,
            profileImagePainter,
            modifier = Modifier.size(width = 300.dp, height = 380.dp),
        )
    }
}
