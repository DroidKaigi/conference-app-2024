package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardTheme
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState

@Composable
internal fun ShareableCard(
    uiState: ProfileCardUiState.Card,
    graphicsLayer: GraphicsLayer,
    profileImagePainter: AsyncImagePainter,
    qrCodeImagePainter: AsyncImagePainter,
    onReadyShare: () -> Unit,
) {
    var frontImage: ImageBitmap? by remember { mutableStateOf(null) }
    var backImage: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(frontImage, backImage) {
        if (frontImage != null && backImage != null) {
            onReadyShare()
        }
    }

    BackgroundCapturableCardFront(
        uiState = uiState,
        profileImagePainter = profileImagePainter,
    ) {
        frontImage = it
    }

    BackgroundCapturableCardBack(
        uiState = uiState,
        qrCodeImagePainter = qrCodeImagePainter,
    ) {
        backImage = it
    }

    Box(
        modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(LocalProfileCardTheme.current.primaryColor)
                .padding(vertical = 50.dp, horizontal = 120.dp),
        ) {
            backImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 70.dp, y = 15.dp)
                        .rotate(10f)
                        .size(150.dp, 190.dp),
                )
            }
            frontImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = (-70).dp, y = (-15).dp)
                        .rotate(-10f)
                        .size(150.dp, 190.dp),
                )
            }
        }
    }
}
