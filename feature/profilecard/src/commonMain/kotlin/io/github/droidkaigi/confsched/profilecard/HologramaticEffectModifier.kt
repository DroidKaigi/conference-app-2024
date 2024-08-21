package io.github.droidkaigi.confsched.profilecard

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.IntSize

fun Modifier.hologramaticEffect() = this then HologramaticEffectElement

private data object HologramaticEffectElement : ModifierNodeElement<HologramaticEffectNode>() {
    override fun create(): HologramaticEffectNode {
        return HologramaticEffectNode()
    }

    override fun update(node: HologramaticEffectNode) {
//        node.callback = onGloballyPositioned
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "HologramaticEffectNode"
//        properties["HologramaticEffectNode"] = onGloballyPositioned
    }
}

private class HologramaticEffectNode() :
    Modifier.Node(),
    GlobalPositionAwareModifierNode,
    DrawModifierNode {
    private var size = IntSize.Zero

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        size = coordinates.size
    }

    override fun ContentDrawScope.draw() {
        drawInFrontOf {
            drawRect(
                brush = Brush.linearGradient(
                    colors = GRADIENT_COLORS,
                    start = Offset(size.width * 1.50f, -size.height * 0.50f),
                    end = Offset(-size.width * 0.50f, size.height * 1.50f),
                ),
                alpha = 0.1f,
            )
            drawRect(
                brush = Brush.linearGradient(
                    colors = LIGHT_COLORS,
                    start = Offset(-size.width * 0.50f, -size.height * 0.50f),
                    end = Offset(size.width * 1.50f, size.height * 1.50f),
                ),
                alpha = 0.3f,
            )
        }
    }

    companion object {
        private val Transparent = Color(0, 0, 0, 0)
        private val LightGreen = Color(194, 255, 182)
        private val LightPink = Color(255, 163, 182)
        private val LightPurple = Color(221, 169, 255)
        private val LightBlue = Color(162, 209, 255)

        private val GRADIENT_COLORS = listOf(
            LightGreen,
            LightPink,
            LightPurple,
            LightBlue,
        )

        private val LIGHT_COLORS = (Color.White * 4).map { Pair(it, Transparent) }.flatMap { it.toList() }
    }
}

private fun ContentDrawScope.drawInFrontOf(
    frontContent: ContentDrawScope.() -> Unit,
) {
    drawContent()
    frontContent()
}

private operator fun Color.times(times : Int): List<Color> {
    return List(times) { this }
}
