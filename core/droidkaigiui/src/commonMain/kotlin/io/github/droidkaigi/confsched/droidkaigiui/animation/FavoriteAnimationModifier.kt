package io.github.droidkaigi.confsched.droidkaigiui.animation

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.platform.InspectorInfo

fun Modifier.onGloballyPositionedWithFavoriteAnimationScope(
    onGloballyPositioned: (FavoriteAnimationScope?, LayoutCoordinates) -> Unit,
) = this then OnGloballyPositionedElement(onGloballyPositioned)

private data class OnGloballyPositionedElement(
    val onGloballyPositioned: (FavoriteAnimationScope?, LayoutCoordinates) -> Unit,
) : ModifierNodeElement<OnGloballyPositionedNode>() {

    override fun create(): OnGloballyPositionedNode {
        return OnGloballyPositionedNode(onGloballyPositioned)
    }

    override fun update(node: OnGloballyPositionedNode) {
        node.callback = onGloballyPositioned
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "onGloballyPositioned"
        properties["onGloballyPositioned"] = onGloballyPositioned
    }
}

private class OnGloballyPositionedNode(
    var callback: (FavoriteAnimationScope?, LayoutCoordinates) -> Unit,
) : Modifier.Node(),
    ObserverModifierNode,
    CompositionLocalConsumerModifierNode,
    GlobalPositionAwareModifierNode {
    private var favoriteAnimationScope: FavoriteAnimationScope? = null

    private fun updateFavoriteAnimationScope() {
        val scope = currentValueOf(LocalFavoriteAnimationScope)
        favoriteAnimationScope = scope
    }

    override fun onAttach() {
        updateFavoriteAnimationScope()
        observeReads {
            currentValueOf(LocalFavoriteAnimationScope)
        }
    }

    override fun onObservedReadsChanged() {
        updateFavoriteAnimationScope()
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        callback(favoriteAnimationScope, coordinates)
    }
}
