package io.github.droidkaigi.confsched.main.section

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.animation.onGloballyPositionedWithFavoriteAnimationScope
import io.github.droidkaigi.confsched.droidkaigiui.useIf
import io.github.droidkaigi.confsched.main.MainScreenTab
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GlassLikeNavRail(
    hazeState: HazeState,
    onTabSelected: (MainScreenTab) -> Unit,
    currentTab: MainScreenTab,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(width = 64.dp, height = 320.dp)
            .hazeChild(state = hazeState, shape = CircleShape).border(
                width = Dp.Hairline,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = .8f),
                        Color.White.copy(alpha = .2f),
                    ),
                ),
                shape = CircleShape,
            ),
    ) {
        NavRailTabs(
            selectedTab = currentTab.ordinal,
            onTabSelected = { onTabSelected(it) },
        )

        val animatedSelectedTabIndex by animateFloatAsState(
            targetValue = currentTab.ordinal.toFloat(),
            label = "animatedSelectedTabIndex",
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioLowBouncy,
            ),
        )

        val animatedColor by animateColorAsState(
            // FIXME: apply theme
            targetValue = Color(0xFF67FF8D),
            label = "animatedColor",
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
            ),
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
        ) {
            val tabWidth = size.height / MainScreenTab.size
            drawCircle(
                color = animatedColor.copy(alpha = .6f),
                radius = size.width / 2,
                center =
                Offset(
                    size.width / 2,
                    (tabWidth * animatedSelectedTabIndex) + tabWidth / 2,
                ),
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
        ) {
            val path =
                Path().apply {
                    addRoundRect(RoundRect(size.toRect(), CornerRadius(size.width)))
                }
            val length = PathMeasure().apply { setPath(path, false) }.length

            val tabWidth = size.height / MainScreenTab.size
            val startY = tabWidth * (animatedSelectedTabIndex + 1)
            val endY = tabWidth * animatedSelectedTabIndex
            drawPath(
                path,
                brush =
                Brush.verticalGradient(
                    colors =
                    listOf(
                        animatedColor.copy(alpha = 0f),
                        animatedColor.copy(alpha = 1f),
                        animatedColor.copy(alpha = 1f),
                        animatedColor.copy(alpha = 0f),
                    ),
                    startY = startY,
                    endY = endY,
                ),
                style =
                Stroke(
                    width = 6f,
                    pathEffect =
                    PathEffect.dashPathEffect(
                        intervals = floatArrayOf(length / 2, length),
                    ),
                ),
            )
        }
    }
}

@Composable
fun NavRailTabs(
    selectedTab: Int,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        ),
        LocalContentColor provides Color.White,
    ) {
        Column(
            modifier = modifier.padding(vertical = 12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (tab in MainScreenTab.entries) {
                val alpha by animateFloatAsState(
                    targetValue = if (selectedTab == MainScreenTab.indexOf(tab)) 1f else .35f,
                    label = "alpha",
                )
                val scale by animateFloatAsState(
                    targetValue = if (selectedTab == MainScreenTab.indexOf(tab)) 1f else .98f,
                    visibilityThreshold = .000001f,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                    label = "scale",
                )
                Column(
                    modifier = Modifier.scale(scale).alpha(alpha).weight(1f).pointerInput(Unit) {
                        detectTapGestures {
                            onTabSelected(tab)
                        }
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val iconRes = if (selectedTab == MainScreenTab.indexOf(tab)) {
                        tab.iconOn
                    } else {
                        tab.iconOff
                    }
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = "tab ${stringResource(tab.contentDescription)}",
                        modifier = Modifier.useIf(
                            tab == MainScreenTab.Favorite,
                        ) {
                            onGloballyPositionedWithFavoriteAnimationScope { scope, coordinates ->
                                val position = coordinates.positionInRoot()
                                scope?.setTargetPosition(position)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GlassLikeNavRailPreview() {
    val hazeState = remember { HazeState() }

    KaigiTheme {
        Scaffold {
            GlassLikeNavRail(
                hazeState = hazeState,
                {},
                MainScreenTab.Timetable,
            )
        }
    }
}
