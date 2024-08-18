package io.github.droidkaigi.confsched.ui.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.primaryFixed
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalClock
import io.github.droidkaigi.confsched.ui.drawInFrontOf

private val IconSize = 24.0.dp

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

    val favoriteIconColor = MaterialTheme.colorScheme.primaryFixed
    val painter = rememberVectorPainter(Icons.Filled.Favorite)

    CompositionLocalProvider(
        LocalFavoriteAnimationScope provides favoriteAnimationScope,
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .onGloballyPositionedWithFavoriteAnimationScope { scope, coordinates ->
                    val position = coordinates.positionInRoot()
                    scope?.setAnimationFramePosition(position)
                }.drawInFrontOf {
                    if (favoriteAnimationScope.animations.isNotEmpty()) {
                        favoriteAnimationScope.animations.forEach {
                            translate(
                                left = it.offset.x,
                                top = it.offset.y,
                            ) {
                                with(painter) {
                                    draw(
                                        size = Size(IconSize.toPx(), IconSize.toPx()),
                                        colorFilter = ColorFilter.tint(favoriteIconColor),
                                    )
                                }
                            }
                        }
                    }
                },
        ) {
            content()
        }
    }
}
