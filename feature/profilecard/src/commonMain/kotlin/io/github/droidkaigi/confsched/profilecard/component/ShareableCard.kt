package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import coil3.compose.AsyncImagePainter
import com.preat.peekaboo.image.picker.toImageBitmap
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalProfileCardTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideProfileCardTheme
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.model.generateColoredImageBase64
import io.github.droidkaigi.confsched.profilecard.ProfileCardUiState
import io.github.droidkaigi.confsched.profilecard.decodeBase64Bytes
import io.github.droidkaigi.confsched.profilecard.toCardUiState
import org.jetbrains.compose.ui.tooling.preview.Preview

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

    ShareableCardContent(
        modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            },
        frontImage = frontImage,
        backImage = backImage,
    )
}

@Composable
private fun ShareableCardContent(
    frontImage: ImageBitmap?,
    backImage: ImageBitmap?,
    modifier: Modifier = Modifier,
) {
    val widthPx = 1200
    val heightPx = 630
    val cardWidthPx = 300
    val cardHeightPx = 380
    val offsetXBackPx = 148f
    val offsetYBackPx = 76f
    val offsetXFrontPx = -136f
    val offsetYFrontPx = -61f
    val verticalPaddingPx = 30f

    val density = LocalDensity.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredSize(
                width = with(density) { widthPx.toDp() },
                height = with(density) { heightPx.toDp() },
            )
            .background(LocalProfileCardTheme.current.primaryColor),
    ) {
        Box(modifier = Modifier.padding(vertical = with(density) { verticalPaddingPx.toDp() })) {
            backImage?.let { backBitmap ->
                ShadowedImage(
                    imageBitmap = backBitmap,
                    offsetX = offsetXBackPx,
                    offsetY = offsetYBackPx,
                    rotation = 10f,
                    cardWidthPx = cardWidthPx,
                    cardHeightPx = cardHeightPx,
                )
            }
            frontImage?.let { frontBitmap ->
                ShadowedImage(
                    imageBitmap = frontBitmap,
                    offsetX = offsetXFrontPx,
                    offsetY = offsetYFrontPx,
                    rotation = -12.2f,
                    cardWidthPx = cardWidthPx,
                    cardHeightPx = cardHeightPx,
                )
            }
        }
    }
}

@Composable
private fun ShadowedImage(
    imageBitmap: ImageBitmap,
    offsetX: Float,
    offsetY: Float,
    rotation: Float,
    cardWidthPx: Int,
    cardHeightPx: Int,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .offset(
                x = with(density) { offsetX.toDp() },
                y = with(density) { offsetY.toDp() },
            )
            .rotate(rotation)
            .size(
                width = with(density) { cardWidthPx.toDp() },
                height = with(density) { cardHeightPx.toDp() },
            )
            .drawWithContent {
                // Draw the blurred shadow behind the actual content.
                // This function first draws a shadow by calling drawBlurredShadow(),
                // then draws the actual content (the image) with drawContent().
                // By doing this, we ensure the shadow appears behind the image,
                // creating a layered effect with the shadow in the background.
                drawBlurredShadow(size, Color.Black.copy(alpha = 0.2f))
                // The child element Image is drawn by calling drawContent.
                drawContent()
            },
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier.graphicsLayer(),
        )
    }
}

/**
 * Draws a blurred shadow effect on the canvas.
 *
 * This function creates a blurred shadow by drawing multiple layers of rectangles with varying offsets
 * and transparency. The shadow effect is created by gradually increasing the offset and reducing the
 * opacity of each rectangle, simulating the natural fading of a shadow.
 *
 * @param size The size of the base rectangle to draw the shadow around.
 * @param color The base color of the shadow. The alpha channel will be adjusted for the blur effect.
 */
private fun ContentDrawScope.drawBlurredShadow(size: Size, color: Color) {
    // The number of shadow layers determines the smoothness of the shadow.
    // Fewer layers result in a less smooth shadow.
    val shadowLayers = 30

    // Controls how much the shadow spreads out.
    // A larger number results in a more diffused and smoother shadow.
    val maxOffset = 20f

    for (i in 1..shadowLayers) {
        val offset = i * (maxOffset / shadowLayers)
        drawRect(
            // The shadow is darkest at its base, and becomes lighter as it extends further away.
            color = color.copy(alpha = 0.05f / i),
            // The shadow extends diagonally from the upper left to the lower right.
            topLeft = Offset(offset, offset),
            // Increasing the size of the rectangles with each iteration creates the effect of a shadow
            // that gets thinner and more diffused the further it is from the base.
            size = Size(size.width + offset, size.height + offset),
        )
    }
}

@Composable
@Preview
fun ShareableCardPreview() {
    val uiState = ProfileCard.Exists.fake().toCardUiState()!!
    val frontImage = generateColoredImageBase64(
        color = Color.LightGray,
        size = IntSize(300, 380),
    ).decodeBase64Bytes().toImageBitmap()
    val backImage = generateColoredImageBase64(
        color = Color.DarkGray,
        size = IntSize(300, 380),
    ).decodeBase64Bytes().toImageBitmap()

    KaigiTheme {
        Surface {
            ProvideProfileCardTheme(uiState.cardType.name) {
                ShareableCardContent(frontImage, backImage)
            }
        }
    }
}
