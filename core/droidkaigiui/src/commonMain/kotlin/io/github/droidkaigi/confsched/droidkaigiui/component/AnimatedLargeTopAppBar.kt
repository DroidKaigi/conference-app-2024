package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedLargeTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    navIconContentDescription: String?,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors().copy(
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    ),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val density = LocalDensity.current.density
    var navigationIconWidthDp by remember { mutableStateOf(0f) }
    val isCenterTitle = remember(scrollBehavior?.state?.collapsedFraction) {
        if (scrollBehavior == null) {
            // Always left-align when scrollBehavior is not present
            false
        } else {
            // Hide title position because it doesn't look smooth if it is displayed when collapsing
            when (scrollBehavior.state.collapsedFraction) {
                in 0.7f..1.0f -> true
                in 0.0f..0.5f -> false
                else -> null // Don't display while on the move.
            }
        }
    }

    LargeTopAppBar(
        title = {
            AnimatedVisibility(
                visible = isCenterTitle != null,
                enter = fadeIn(),
                // No animation required as it is erased with alpha
                exit = ExitTransition.None,
            ) {
                Text(
                    text = title,
                    modifier = Modifier.then(
                        when (isCenterTitle) {
                            true -> {
                                Modifier
                                    .padding(end = navigationIconWidthDp.dp)
                                    .fillMaxWidth()
                            }
                            false -> Modifier
                            null -> Modifier.alpha(0f)
                        },
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .onGloballyPositioned {
                        navigationIconWidthDp = it.size.width / density
                    },
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Filled.ArrowBack,
                    contentDescription = navIconContentDescription,
                )
            }
        },
        actions = actions,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}
