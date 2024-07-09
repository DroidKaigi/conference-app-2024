package io.github.droidkaigi.confsched.main.bottom.navigation

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.feature.main.generated.resources.Res
import conference_app_2024.feature.main.generated.resources.icon_map_fill
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.main.IconRepresentation
import io.github.droidkaigi.confsched.main.IconRepresentation.Drawable
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun GlassLikeBottomNavigation(hazeState: HazeState) {
    var selectedTabIndex by remember { mutableIntStateOf(1) }
    Box(
        modifier =
            Modifier
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
            tabs,
            selectedTab = selectedTabIndex,
            onTabSelected = {
                selectedTabIndex = tabs.indexOf(it)
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
            targetValue = tabs[selectedTabIndex].color,
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
            val tabWidth = size.width / tabs.size
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

            val tabWidth = size.width / tabs.size
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BottomBarTabs(
    tabs: List<BottomBarTab>,
    selectedTab: Int,
    onTabSelected: (BottomBarTab) -> Unit,
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
            modifier = Modifier.fillMaxSize(),
        ) {
            for (tab in tabs) {
                val alpha by animateFloatAsState(
                    targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .35f,
                    label = "alpha",
                )
                val scale by animateFloatAsState(
                    targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .98f,
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
                    Icon(imageVector = tab.icon, contentDescription = "tab ${tab.title}")
                    Text(text = tab.title)
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
            GlassLikeBottomNavigation(hazeState = hazeState)
        }
    }
}

sealed class BottomBarTab(val title: String, val icon: Drawable, val color: Color) {
    data object Timetable : BottomBarTab(
        title = "Timetable",
        icon = IconRepresentation.Drawable(drawableId = Res.drawable.calendar_month),
        color = Color(0xFFFFA574),
    )

    data object EventMap : BottomBarTab(
        title = "Timetable",
        icon = IconRepresentation.Drawable(drawableId = Res.),
        color = Color(0xFFFFA574),
    )

    data object Favorite : BottomBarTab(
        title = "Favorite",
        icon = IconRepresentation.Drawable(drawableId = Res.drawable.icon_map_fill),
        color = Color(0xFFFA6FFF),
    )

    data object Info : BottomBarTab(
        title = "Settings",
        icon = Icons.Rounded.Info,
        color = Color(0xFFADFF64),
    )
    data object Settings : BottomBarTab(
        title = "Settings",
        icon = Icons.Rounded.Settings,
        color = Color(0xFFADFF64),
    )
}

val tabs =
    listOf(
        BottomBarTab.Timetable,
        BottomBarTab.EventMap,
        BottomBarTab.Favarite,
        BottomBarTab.Info,
        BottomBarTab.Settings,
    )
