package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.toImageBitmap
import io.github.droidkaigi.confsched.model.ProfileImage
import io.github.droidkaigi.confsched.profilecard.TransformCalculator

@Composable
internal fun ImageCropAreaSelector(
    profileImage: ProfileImage,
    transformCalculator: TransformCalculator,
    scale: () -> Float,
    offset: () -> Offset,
    onTransform: (scale: Float, offset: Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strokeWidth = with(LocalDensity.current) { 2.dp.toPx() }
    val strokeOuterColor = MaterialTheme.colorScheme.onSurface
    val strokeInnerColor = MaterialTheme.colorScheme.surface

    Image(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(profileImage) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val (newScale, newOffset) = transformCalculator.calculateNext(
                        oldScale = scale(),
                        oldOffset = offset(),
                        zoom = zoom,
                        pan = pan,
                    )

                    onTransform(newScale, newOffset)
                }
            }
            .drawWithContent {
                drawContent()

                val length = transformCalculator.cropRectLength
                val outerRect = Size(length, length)
                    .toRect()
                    .translate(size.center)
                    .translate(-length / 2, -length / 2)
                val innerRect = outerRect.deflate(strokeWidth)

                drawRect(
                    color = strokeOuterColor,
                    topLeft = outerRect.topLeft,
                    size = outerRect.size,
                    style = Stroke(width = strokeWidth),
                )
                drawRect(
                    color = strokeInnerColor,
                    topLeft = innerRect.topLeft,
                    size = innerRect.size,
                    style = Stroke(width = strokeWidth),
                )
            }
            .graphicsLayer {
                val currentScale = scale()
                val currentOffset = offset()

                scaleX = currentScale
                scaleY = currentScale
                translationX = -currentOffset.x * currentScale
                translationY = -currentOffset.y * currentScale
            },
        bitmap = profileImage.bytes.toImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )
}
