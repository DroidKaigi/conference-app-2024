package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImagePainter
import com.preat.peekaboo.image.picker.toImageBitmap
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalClock
import io.github.droidkaigi.confsched.model.generateColoredImageBase64
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import io.github.droidkaigi.confsched.profilecard.decodeBase64Bytes
import kotlinx.coroutines.flow.first

@Composable
internal fun BackgroundCapturableCardBack(
    uiState: Card,
    qrCodeImagePainter: AsyncImagePainter,
    onCaptured: (ImageBitmap) -> Unit,
) {
    val clock = LocalClock.current
    val graphicsLayer: GraphicsLayer = rememberGraphicsLayer()
    var lastCaptureTime by remember { mutableStateOf(0L) }

    LaunchedEffect(lastCaptureTime) {
        if (lastCaptureTime == 0L) {
            return@LaunchedEffect
        }
        qrCodeImagePainter.state.first { it is AsyncImagePainter.State.Success }
        Logger.d { "BackgroundCapturableCardBack: onCaptured" }
        try {
            // FIXME In the tests currently implemented, an IllegalArgumentException is thrown when SkiaGraphicsLayer.toImageBitmap is executed.
            // FIXME https://github.com/DroidKaigi/conference-app-2024/pull/984#issuecomment-2335126855
            onCaptured(graphicsLayer.toImageBitmap())
        } catch (e: IllegalArgumentException) {
            val workAroundImageBitmap = generateColoredImageBase64()
                .decodeBase64Bytes()
                .toImageBitmap()
            onCaptured(workAroundImageBitmap)
            Logger.e("In the tests currently implemented, an IllegalArgumentException is thrown when SkiaGraphicsLayer.toImageBitmap is executed.", e)
        }
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
        CompositionLocalProvider(
            LocalDensity provides Density(
                density = 1f,
                fontScale = 1f,
            ),
        ) {
            FlipCardBack(
                uiState,
                qrCodeImagePainter,
                modifier = Modifier
                    .size(width = 300.dp, height = 380.dp)
                    .border(
                        3.dp,
                        Color.Black,
                        RoundedCornerShape(8.dp),
                    )
                    .graphicsLayer {
                        rotationY = 180f
                    },
            )
        }
    }
}
