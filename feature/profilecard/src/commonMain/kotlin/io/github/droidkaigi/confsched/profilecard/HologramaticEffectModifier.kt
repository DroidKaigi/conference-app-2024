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
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.DeviceOrientationScope
import io.github.droidkaigi.confsched.droidkaigiui.Orientation
import kotlin.math.absoluteValue

fun Modifier.hologramaticEffect(deviceOrientationScope: DeviceOrientationScope) = this then HologramaticEffectElement(
    orientation = deviceOrientationScope.orientation,
)

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
    DrawModifierNode {

    override fun ContentDrawScope.draw() {
        val pitchDegree = radianToDegree(orientation.pitch)
        val rollDegree = radianToDegree(orientation.roll)

        val pitchRatio = pitchDegree / 90f
        val rollRatio = rollDegree / 180f

        drawInFrontOf {
            DrawPathConfigs.forEach { config ->
                translate(
                    left = -(size.width * 0.5f) * rollRatio / 2f,
                    top = (size.height * 0.5f) * pitchRatio * config.speed,
                ) {
                    if (rollDegree in -80f..80f) {
                        val alpha = config.alpha * (80f - rollDegree.absoluteValue) / 80f
                        val offset = config.offset.toPx()

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
                                    0.5f - config.width / 2f to config.startColor,
                                    0.5f to Color.White,
                                    0.5f + config.width / 2f to config.endColor,
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
        return (radian * 180f / Pi).toFloat()
    }

    private data class DrawPathConfig(
        val offset: Dp,
        val startColor: Color,
        val endColor: Color,
        @FloatRange(from = 0.0, to = 0.1)
        val width: Float,
        @FloatRange(from = 0.0, to = 1.0)
        val alpha: Float,
        @FloatRange(from = 0.0, to = 1.0)
        val speed: Float,
    ) {
        constructor(
            offset: Dp,
            color: Color,
            width: Float,
            alpha: Float,
            speed: Float,
        ) : this(
            offset = offset,
            startColor = color,
            endColor = color,
            width = width,
            alpha = alpha,
            speed = speed,
        )
    }

    companion object {

        private const val Pi = 3.14159265359

        private val Transparent = Color(0, 0, 0, 0)
        private val LightGreen = Color(0xFF45E761)
        private val LightPink = Color(0xFFFF53CF)
        private val LightPurple = Color(0xFF9B51E0)
        private val LightBlue = Color(0xFF44ADE7)

        private val DrawPathConfigs = listOf(
            DrawPathConfig(
                offset = ((-450).dp),
                startColor = LightGreen,
                endColor = LightPink,
                width = 0.03f,
                alpha = 0.3f,
                speed = 1.0f,
            ),
            DrawPathConfig(
                offset = (-200).dp,
                color = LightPurple,
                width = 0.02f,
                alpha = 0.4f,
                speed = 0.6f,
            ),
            DrawPathConfig(
                offset = 50.dp,
                color = LightBlue,
                width = 0.01f,
                alpha = 0.5f,
                speed = 0.3f,
            ),
        )
    }
}

private fun ContentDrawScope.drawInFrontOf(
    frontContent: ContentDrawScope.() -> Unit,
) {
    drawContent()
    frontContent()
}
