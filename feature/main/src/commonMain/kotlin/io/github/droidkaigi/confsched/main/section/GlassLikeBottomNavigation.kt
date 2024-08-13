package io.github.droidkaigi.confsched.main.section

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.primaryFixed
import io.github.droidkaigi.confsched.main.MainScreenTab
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GlassLikeBottomNavigation(
    hazeState: HazeState,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    Box(
        modifier = modifier
            .padding(horizontal = 48.dp)
            .fillMaxWidth()
            .height(64.dp)
            .hazeChild(state = hazeState, shape = CircleShape)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = .3f),
                shape = CircleShape,
            )
            .padding(horizontal = 12.dp),
    ) {
        BottomBarTabs(
            selectedTab = selectedTabIndex,
            onTabSelected = {
                selectedTabIndex = MainScreenTab.indexOf(it)
                onTabSelected(it)
            },
        )
    }
}

@Composable
fun BottomBarTabs(
    selectedTab: Int,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalTextStyle provides
            LocalTextStyle.current.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            ),
        LocalContentColor provides Color.White,
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
        ) {
            for (tab in MainScreenTab.values()) {
                Column(
                    modifier =
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onTabSelected(tab)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = tab.icon.imageVector,
                        tint = if (selectedTab == MainScreenTab.indexOf(tab)) {
                            MaterialTheme.colorScheme.primaryFixed
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        contentDescription = "tab ${stringResource(tab.contentDescription)}",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GlassLikeBottomNavigationPreview() {
    val hazeState = remember { HazeState() }

    KaigiTheme {
        Scaffold {
            GlassLikeBottomNavigation(
                hazeState = hazeState,
                {},
            )
        }
    }
}
