package io.github.droidkaigi.confsched.profilecard

import androidx.annotation.FloatRange
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.ui.DeviceOrientationScope
import io.github.droidkaigi.confsched.ui.Orientation
import kotlin.math.absoluteValue

context(DeviceOrientationScope)
fun Modifier.hologramaticEffect() = this then HologramaticEffectElement(orientation)

private data class HologramaticEffectElement(
    private val orientation: Orientation,
) : ModifierNodeElement<HologramaticEffectNode>() {
    override fun create(): HologramaticEffectNode {
        return HologramaticEffectNode(orientation)
    }

    override fun update(node: HologramaticEffectNode) {
        node.orientation = orientation
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "HologramaticEffectNode"
        properties["orientation"] = orientation
    }
}

private data class HologramaticEffectNode(
    var orientation: Orientation,
) :
    Modifier.Node(),
    GlobalPositionAwareModifierNode,
    DrawModifierNode {

    private var size = IntSize.Zero

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        size = coordinates.size
    }

    override fun ContentDrawScope.draw() {
        val pitchDegree = radianToDegree(orientation.pitch)
        val rollDegree = radianToDegree(orientation.roll)

        val pitchRatio = pitchDegree / 90f
        val rollRatio = rollDegree / 180f

        drawInFrontOf {
            translate(
                left = -(size.width * 0.5f) * rollRatio / 2f,
                top = (size.height * 0.5f) * pitchRatio,
            ) {
                if (rollDegree in -80f..80f) {
                    DRAW_PATH_STATES.forEach { state ->
                        val alpha = state.alpha * (80f - rollDegree.absoluteValue) / 80f
                        val offset = state.offset.toPx()

                        drawRect(
                            topLeft = Offset(
                                -size.width * 0.5f,
                                -size.height * 0.5f,
                            ),
                            size = Size(
                                size.width * 2f,
                                size.height * 2f,
                            ),
                            brush = Brush.linearGradient(
                                colorStops = arrayOf(
                                    0.0f to Transparent,
                                    0.45f to Transparent,
                                    0.5f - state.width / 2f to state.startColor,
                                    0.5f to Color.White,
                                    0.5f + state.width / 2f to state.endColor,
                                    0.55f to Transparent,
                                    1.0f to Transparent,
                                ),
                                start = Offset(
                                    offset.coerceAtMost(0f),
                                    offset.coerceAtMost(0f),
                                ),
                                end = Offset(
                                    size.width * 2.0f + offset.coerceAtLeast(0f),
                                    size.width * 2.0f + offset.coerceAtLeast(0f),
                                ),
                            ),
                            alpha = alpha,
                            blendMode = BlendMode.Lighten,
                        )
                    }
                }
            }
        }
    }

    private fun radianToDegree(radian: Float): Float {
        return (radian * 180f / PI).toFloat()
    }

    private data class DrawPathState(
        val offset: Dp,
        val startColor: Color,
        val endColor: Color,
        @FloatRange(from = 0.0, to = 0.1)
        val width: Float,
        @FloatRange(from = 0.0, to = 1.0)
        val alpha: Float,
    ) {
        constructor(
            offset: Dp,
            color: Color,
            width: Float,
            alpha: Float,
        ) : this(
            offset = offset,
            startColor = color,
            endColor = color,
            width = width,
            alpha = alpha
        )
    }

    companion object {

        private const val PI = 3.14159265359

        private val Transparent = Color(0, 0, 0, 0)
        private val LightGreen = Color(194, 255, 182)
        private val LightPurple = Color(221, 169, 255)
        private val LightBlue = Color(162, 209, 255)

        private val DRAW_PATH_STATES = listOf(
            DrawPathState(((-450).dp), LightGreen, LightBlue, 0.03f, 0.3f),
            DrawPathState((-200).dp, LightBlue, 0.02f, 0.4f),
            DrawPathState(50.dp, LightPurple, 0.01f, 0.5f),
        )
    }
}

private fun ContentDrawScope.drawInFrontOf(
    frontContent: ContentDrawScope.() -> Unit,
) {
    drawContent()
    frontContent()
}
