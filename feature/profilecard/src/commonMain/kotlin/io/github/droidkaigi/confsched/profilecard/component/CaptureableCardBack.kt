package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import kotlinx.coroutines.delay

@Composable
internal fun CapturableCardBackEffect(
    uiState: Card,
    qrCodeImagePainter: Painter,
    onCaptured: (ImageBitmap) -> Unit,
) {
    val graphicsLayer: GraphicsLayer = rememberGraphicsLayer()
    var isBackCaptured by remember { mutableStateOf(false) }
    var isBackSizeNonZero by remember { mutableStateOf(false) }

    LaunchedEffect(isBackCaptured, isBackSizeNonZero) {
        // In ComposableMultiplatform, an ImageBitmap is not Null, but may come with a size of 0.
        // If the process reaches the Image's Composable with a size of 0, the application will crash with the following error.
        // Uncaught Kotlin exception: kotlin.IllegalStateException: Size is unspecified
        Logger.d { "isBackCaptured: $isBackCaptured, isBackSizeNonZero: $isBackSizeNonZero" }
        if (isBackCaptured.not() || isBackSizeNonZero.not()) {
            return@LaunchedEffect
        }

        // after qr code rendered with logo, tell the event to parent component
        delay(300)
        onCaptured(graphicsLayer.toImageBitmap())
    }

    Box(
        modifier = Modifier
            .drawWithCache {
                onDrawWithContent {
                    graphicsLayer.record {
                        this@onDrawWithContent.drawContent()
                    }
                    isBackSizeNonZero =
                        graphicsLayer.size.width > 0 && graphicsLayer.size.height > 0
                    drawLayer(graphicsLayer)
                }
            }
            .onGloballyPositioned {
                isBackCaptured = true
            },
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
