package io.github.droidkaigi.confsched.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import conference_app_2024.feature.main.generated.resources.Res
import conference_app_2024.feature.main.generated.resources.icon_achievement_fill
import conference_app_2024.feature.main.generated.resources.icon_achievement_outline
import conference_app_2024.feature.main.generated.resources.icon_map_fill
import io.github.droidkaigi.confsched.compose.EventEmitter
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.main.NavigationType.BottomNavigation
import io.github.droidkaigi.confsched.main.NavigationType.NavigationRail
import io.github.droidkaigi.confsched.main.strings.MainStrings
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

const val mainScreenRoute = "main"
const val MainScreenTestTag = "MainScreen"

fun NavGraphBuilder.mainScreen(
    windowSize: WindowSizeClass,
    mainNestedGraphStateHolder: MainNestedGraphStateHolder,
    mainNestedGraph: NavGraphBuilder.(mainNestedNavController: NavController, PaddingValues) -> Unit,
) {
    composable(mainScreenRoute) {
        MainScreen(
            windowSize = windowSize,
            mainNestedGraphStateHolder = mainNestedGraphStateHolder,
            mainNestedNavGraph = mainNestedGraph,
        )
    }
}

interface MainNestedGraphStateHolder {
    val startDestination: String
    fun routeToTab(route: String): MainScreenTab?
    fun onTabSelected(mainNestedNavController: NavController, tab: MainScreenTab)
}

enum class NavigationType {
    BottomNavigation, NavigationRail
}

@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    mainNestedGraphStateHolder: MainNestedGraphStateHolder,
    mainNestedNavGraph: NavGraphBuilder.(NavController, PaddingValues) -> Unit,
    eventEmitter: EventEmitter<MainScreenEvent> = rememberEventEmitter(),
    uiState: MainScreenUiState = mainScreenPresenter(eventEmitter),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val navigationType: NavigationType = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> BottomNavigation
        WindowWidthSizeClass.Medium -> NavigationRail
        WindowWidthSizeClass.Expanded -> NavigationRail
        else -> BottomNavigation
    }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    MainScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        navigationType = navigationType,
        routeToTab = mainNestedGraphStateHolder::routeToTab,
        onTabSelected = mainNestedGraphStateHolder::onTabSelected,
        mainNestedNavGraph = mainNestedNavGraph,
    )
}

sealed class IconRepresentation {
    data class Vector(val imageVector: ImageVector) : IconRepresentation()

    @ExperimentalResourceApi
    data class Drawable(val drawableId: DrawableResource) : IconRepresentation()
}

enum class MainScreenTab(
    val icon: IconRepresentation,
    val selectedIcon: IconRepresentation,
    val label: String,
    val contentDescription: String,
    val testTag: String = "mainScreenTab:$label",
) {
    Timetable(
        icon = IconRepresentation.Vector(Icons.Outlined.CalendarMonth),
        selectedIcon = IconRepresentation.Vector(Icons.Filled.CalendarMonth),
        label = MainStrings.Timetable.asString(),
        contentDescription = MainStrings.Timetable.asString(),
    ),

    @OptIn(ExperimentalResourceApi::class)
    FloorMap(
        icon = IconRepresentation.Vector(Icons.Outlined.Map),
        selectedIcon = IconRepresentation.Drawable(drawableId = Res.drawable.icon_map_fill),
        label = MainStrings.FloorMap.asString(),
        contentDescription = MainStrings.FloorMap.asString(),
    ),

    @OptIn(ExperimentalResourceApi::class)
    Achievements(
        icon = IconRepresentation.Drawable(drawableId = Res.drawable.icon_achievement_outline),
        selectedIcon = IconRepresentation.Drawable(drawableId = Res.drawable.icon_achievement_fill),
        label = MainStrings.Achievements.asString(),
        contentDescription = MainStrings.Achievements.asString(),
    ),
    About(
        icon = IconRepresentation.Vector(Icons.Outlined.Info),
        selectedIcon = IconRepresentation.Vector(Icons.Filled.Info),
        label = MainStrings.About.asString(),
        contentDescription = MainStrings.About.asString(),
    ),
}

data class MainScreenUiState(
    val isAchievementsEnabled: Boolean = false,
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun MainScreen(
    uiState: MainScreenUiState,
    snackbarHostState: SnackbarHostState,
    navigationType: NavigationType,
    routeToTab: String.() -> MainScreenTab?,
    onTabSelected: (NavController, MainScreenTab) -> Unit,
    mainNestedNavGraph: NavGraphBuilder.(NavController, PaddingValues) -> Unit,
) {
    val mainNestedNavController = rememberNavController()
    val navBackStackEntry by mainNestedNavController.currentBackStackEntryAsState()
    val currentTab = navBackStackEntry?.destination?.route?.routeToTab()
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == NavigationRail) {
            Column {
                Text(text = "nav rail")
                MainScreenTab.values().forEach { tab ->
                    Button(onClick = { onTabSelected(mainNestedNavController, tab) }) {
                        Text(text = tab.label + " " + (currentTab == tab))
                    }
                }
            }
        }
        Scaffold(
            bottomBar = {
                AnimatedVisibility(visible = navigationType == BottomNavigation) {
                    Row {
                        MainScreenTab.entries.forEach { tab ->
                            Button(
                                modifier = Modifier.weight(1F),
                                onClick = { onTabSelected(mainNestedNavController, tab) }
                            ) {
                                Text(text = tab.label + " " + (currentTab == tab))
                            }
                        }
                    }

                }
            },
        ) { padding ->
            NavHost(
                navController = mainNestedNavController,
                startDestination = "timetable",
                modifier = Modifier,
                enterTransition = { materialFadeThroughIn() },
                exitTransition = { materialFadeThroughOut() },
            ) {
                mainNestedNavGraph(mainNestedNavController, padding)
            }
        }
    }
}

private fun materialFadeThroughIn(): EnterTransition = fadeIn(
    animationSpec = tween(
        durationMillis = 195,
        delayMillis = 105,
        easing = LinearOutSlowInEasing,
    ),
) + scaleIn(
    animationSpec = tween(
        durationMillis = 195,
        delayMillis = 105,
        easing = LinearOutSlowInEasing,
    ),
    initialScale = 0.92f,
)

private fun materialFadeThroughOut(): ExitTransition = fadeOut(
    animationSpec = tween(
        durationMillis = 105,
        delayMillis = 0,
        easing = FastOutLinearInEasing,
    ),
)
