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
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState.Card
import kotlinx.coroutines.delay

@Composable
internal fun CapturableCardFrontEffect(
    uiState: Card,
    profileImagePainter: Painter,
    onCaptured: (ImageBitmap) -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()
    val isFrontSizeNonZero = { graphicsLayer.size.width > 0 && graphicsLayer.size.height > 0 }
    var isFrontCaptured: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(isFrontCaptured, isFrontSizeNonZero) {
        // In ComposableMultiplatform, an ImageBitmap is not Null, but may come with a size of 0.
        // If the process reaches the Image's Composable with a size of 0, the application will crash with the following error.
        // Uncaught Kotlin exception: kotlin.IllegalStateException: Size is unspecified
        Logger.d {
            "isFrontCaptured: $isFrontCaptured, isFrontSizeNonZero: ${isFrontSizeNonZero()}"
        }
        if (isFrontCaptured.not() || isFrontSizeNonZero().not()) {
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
                    drawLayer(graphicsLayer)
                }
            }
            .onGloballyPositioned {
                isFrontCaptured = true
                Logger.d { "graphicsLayer:$graphicsLayer" }
            },
    ) {
        FlipCardFront(
            uiState,
            profileImagePainter,
            modifier = Modifier
                .size(width = 300.dp, height = 380.dp)
                .border(
                    3.dp,
                    Color.Black,
                    RoundedCornerShape(8.dp),
                ),
        )
    }
}
