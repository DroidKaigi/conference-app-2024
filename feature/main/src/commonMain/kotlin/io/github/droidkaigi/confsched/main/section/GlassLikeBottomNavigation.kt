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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.main.MainScreenTab
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GlassLikeBottomNavigation(
    hazeState: HazeState,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Box(
        modifier = modifier
            .padding(vertical = 24.dp, horizontal = 64.dp)
            .fillMaxWidth()
            .height(64.dp)
            .hazeChild(state = hazeState, shape = CircleShape)
            .border(
                width = Dp.Hairline,
                brush =
                Brush.verticalGradient(
                    colors =
                    listOf(
                        Color.White.copy(alpha = .8f),
                        Color.White.copy(alpha = .2f),
                    ),
                ),
                shape = CircleShape,
            ),
    ) {
        BottomBarTabs(
            selectedTab = selectedTabIndex,
            onTabSelected = {
                selectedTabIndex = MainScreenTab.indexOf(it)
                onTabSelected(it)
            },
        )

        val animatedSelectedTabIndex by animateFloatAsState(
            targetValue = selectedTabIndex.toFloat(),
            label = "animatedSelectedTabIndex",
            animationSpec =
            spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioLowBouncy,
            ),
        )

        val animatedColor by animateColorAsState(
            // FIXME: apply theme
            targetValue = Color(0xFF67FF8D),
            label = "animatedColor",
            animationSpec =
            spring(
                stiffness = Spring.StiffnessLow,
            ),
        )

        Canvas(
            modifier =
            Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
        ) {
            val tabWidth = size.width / MainScreenTab.size
            drawCircle(
                color = animatedColor.copy(alpha = .6f),
                radius = size.height / 2,
                center =
                Offset(
                    (tabWidth * animatedSelectedTabIndex) + tabWidth / 2,
                    size.height / 2,
                ),
            )
        }

        Canvas(
            modifier =
            Modifier
                .fillMaxSize()
                .clip(CircleShape),
        ) {
            val path =
                Path().apply {
                    addRoundRect(RoundRect(size.toRect(), CornerRadius(size.height)))
                }
            val length = PathMeasure().apply { setPath(path, false) }.length

            val tabWidth = size.width / MainScreenTab.size
            drawPath(
                path,
                brush =
                Brush.horizontalGradient(
                    colors =
                    listOf(
                        animatedColor.copy(alpha = 0f),
                        animatedColor.copy(alpha = 1f),
                        animatedColor.copy(alpha = 1f),
                        animatedColor.copy(alpha = 0f),
                    ),
                    startX = tabWidth * animatedSelectedTabIndex,
                    endX = tabWidth * (animatedSelectedTabIndex + 1),
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
                val alpha by animateFloatAsState(
                    targetValue = if (selectedTab == MainScreenTab.indexOf(tab)) 1f else .35f,
                    label = "alpha",
                )
                val scale by animateFloatAsState(
                    targetValue = if (selectedTab == MainScreenTab.indexOf(tab)) 1f else .98f,
                    visibilityThreshold = .000001f,
                    animationSpec =
                    spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                    label = "scale",
                )
                Column(
                    modifier =
                    Modifier
                        .scale(scale)
                        .alpha(alpha)
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
                    Icon(imageVector = tab.icon.imageVector, contentDescription = "tab ${tab.contentDescription}")
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
