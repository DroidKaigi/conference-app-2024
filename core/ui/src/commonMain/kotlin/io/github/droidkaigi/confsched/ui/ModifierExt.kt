package io.github.droidkaigi.confsched.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

/**
 * draw round rect behind the modified content.
 *
 * @param color The color to be applied to the rounded rectangle
 * @param cornerRadius Corner radius of the rounded rectangle in dp
 * @param strokeWidth the width of the stroke in dp
 * @param intervals Array of "on" and "off" distances for the dashed line segments
 */
fun Modifier.dashedRoundRect(
    color: Color,
    cornerRadius: Dp,
    strokeWidth: Dp,
    intervals: Array<Dp>,
) = this.drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = intervals.map { it.toPx() }.toFloatArray(),
                phase = 0f,
            ),
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx()),
    )
}
