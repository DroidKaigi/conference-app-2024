package io.github.droidkaigi.confsched.ui.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.positionInRoot

@Composable
fun ProvideFavoriteAnimation(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalFavoriteAnimationScope provides FavoriteAnimationScope(),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
            FavoriteAnimationFrame()
        }
    }
}

@Composable
private fun FavoriteAnimationFrame() {
    Box(
        modifier = Modifier.fillMaxSize()
            .onGloballyPositionedWithFavoriteAnimationScope { scope, coordinates ->
                val position = coordinates.positionInRoot()
                scope?.setAnimationFramePosition(position)
            }
    ) {

    }
}
