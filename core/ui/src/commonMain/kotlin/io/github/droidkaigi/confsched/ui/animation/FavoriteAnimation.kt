package io.github.droidkaigi.confsched.ui.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import conference_app_2024.core.ui.generated.resources.bookmarked
import io.github.droidkaigi.confsched.designsystem.theme.primaryFixed
import io.github.droidkaigi.confsched.ui.UiRes
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalClock
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProvideFavoriteAnimation(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val clock = LocalClock.current
    val coroutineScope = rememberCoroutineScope()
    val favoriteAnimationScope = remember(clock, coroutineScope, isEnabled) {
        FavoriteAnimationScope(
            clock = clock,
            coroutineScope = coroutineScope,
            isEnabled = isEnabled,
        )
    }

    CompositionLocalProvider(
        LocalFavoriteAnimationScope provides favoriteAnimationScope,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            content()
            FavoriteAnimationFrame()
        }
    }
}

@Composable
private fun FavoriteAnimationFrame() {
    val animationScope = LocalFavoriteAnimationScope.current
    Box(
        modifier = Modifier.fillMaxSize()
            .onGloballyPositionedWithFavoriteAnimationScope { scope, coordinates ->
                val position = coordinates.positionInRoot()
                scope?.setAnimationFramePosition(position)
            },
    ) {
        if (animationScope.animations.isNotEmpty()) {
            animationScope.animations.forEach {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = stringResource(UiRes.string.bookmarked),
                    tint = MaterialTheme.colorScheme.primaryFixed,
                    modifier = Modifier
                        .offset {
                            it.offset.toIntOffset()
                        },
                )
            }
        }
    }
}

private fun Offset.toIntOffset(): IntOffset {
    return IntOffset(x.toInt(), y.toInt())
}
