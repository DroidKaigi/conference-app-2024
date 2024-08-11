package io.github.droidkaigi.confsched.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTextTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val isTopState = (scrollBehavior?.state?.overlappedFraction ?: 0f) > 0.01f

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                val horizontalBias by animateFloatAsState(
                    targetValue = if (isTopState) 0f else -1f,
                    animationSpec = tween(durationMillis = 300),
                )
                Text(
                    title,
                    style = if (isTopState) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(BiasAlignment(horizontalBias, 0f)),
                )
            }
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}
