package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItemDetailTopAppBar(
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TimetableItemDetailTopAppBarDefaults.windowInsets(),
) {
    // Allow content to be displayed at the statusBar when scrolling
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val variableTopPaddingHeight by remember(scrollBehavior.state.collapsedFraction) {
        mutableStateOf(statusBarHeight * (1f - scrollBehavior.state.collapsedFraction))
    }

    TopAppBar(
        modifier = modifier
            .background(LocalRoomTheme.current.dimColor)
            .padding(top = variableTopPaddingHeight)
            .consumeWindowInsets(WindowInsets.statusBars),
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = LocalRoomTheme.current.dimColor,
            scrolledContainerColor = LocalRoomTheme.current.dimColor,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
    )
}

object TimetableItemDetailTopAppBarDefaults {
    @Composable
    fun windowInsets() = WindowInsets.displayCutout.union(WindowInsets.systemBars).only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Top,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimetableItemDetailTopAppBarPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailTopAppBar(
                    onNavigationIconClick = {},
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimetableItemDetailTopAppBarUnSelectablePreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailTopAppBar(
                    onNavigationIconClick = {},
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                )
            }
        }
    }
}
