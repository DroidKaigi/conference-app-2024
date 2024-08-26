package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    cornerRadius: Dp = 4.dp,
    strokeWidth: Dp = 1.dp,
    intervals: Array<Dp> = arrayOf(2.dp, 2.dp),
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

/**
 * draw into a Canvas in front of the modified content.
 *
 * @param onDraw The lambda to draw into the Canvas
 *
 */
fun Modifier.drawInFrontOf(
    onDraw: DrawScope.() -> Unit,
): Modifier = this.drawWithContent {
    drawContent()
    onDraw()
}

fun Modifier.useIf(condition: Boolean, apply: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        this.apply()
    } else {
        this
    }
}
