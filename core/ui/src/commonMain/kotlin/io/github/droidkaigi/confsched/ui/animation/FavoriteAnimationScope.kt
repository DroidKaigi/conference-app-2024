package io.github.droidkaigi.confsched.ui.animation

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset

@Suppress("CompositionLocalAllowlist")
internal val LocalFavoriteAnimationScope: ProvidableCompositionLocal<FavoriteAnimationScope> =
    staticCompositionLocalOf {
        error("No FavoriteAnimationScope provided")
    }

internal fun FavoriteAnimationScope(): FavoriteAnimationScope = FavoriteAnimationScopeImpl()

sealed interface FavoriteAnimationScope {
    fun setAnimationFramePosition(offset: Offset)
    fun setTargetPosition(offset: Offset)
}

private class FavoriteAnimationScopeImpl(
) : FavoriteAnimationScope {

    private var animationFramePosition: Offset = Offset.Zero
    private var targetPosition: Offset = Offset.Zero

    override fun setAnimationFramePosition(offset: Offset) {
        animationFramePosition = offset
    }

    override fun setTargetPosition(offset: Offset) {
        targetPosition = offset
    }
}
