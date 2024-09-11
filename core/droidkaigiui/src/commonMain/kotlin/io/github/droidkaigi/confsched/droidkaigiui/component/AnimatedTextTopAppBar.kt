package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import io.github.droidkaigi.confsched.designsystem.component.AutoSizeText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTextTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = AnimatedTextTopAppBarDefaults.windowInsets(),
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors().copy(
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    ),
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val scrollFraction = scrollBehavior?.state?.overlappedFraction ?: 0f
    val transitionFraction = scrollFraction.coerceIn(0f, 1f)

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                AutoSizeText(
                    text = title,
                    color = textColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = 1f - transitionFraction
                        },
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall,
                )

                AutoSizeText(
                    text = title,
                    color = textColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .graphicsLayer {
                            alpha = transitionFraction
                        },
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
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

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior.resetScroll() {
    this.state.heightOffset = 0f
    this.state.contentOffset = 0f
}

object AnimatedTextTopAppBarDefaults {
    @Composable
    fun windowInsets() = WindowInsets.displayCutout.union(WindowInsets.systemBars).only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Top,
    )
}
