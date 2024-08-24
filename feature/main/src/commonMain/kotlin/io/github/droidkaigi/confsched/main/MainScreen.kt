@file:OptIn(ExperimentalResourceApi::class)

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import conference_app_2024.core.designsystem.generated.resources.ic_fav_off
import conference_app_2024.core.designsystem.generated.resources.ic_fav_on
import conference_app_2024.core.designsystem.generated.resources.ic_info_off
import conference_app_2024.core.designsystem.generated.resources.ic_info_on
import conference_app_2024.core.designsystem.generated.resources.ic_map_off
import conference_app_2024.core.designsystem.generated.resources.ic_map_on
import conference_app_2024.core.designsystem.generated.resources.ic_profilecard_off
import conference_app_2024.core.designsystem.generated.resources.ic_profilecard_on
import conference_app_2024.core.designsystem.generated.resources.ic_timetable_off
import conference_app_2024.core.designsystem.generated.resources.ic_timetable_on
import conference_app_2024.feature.main.generated.resources.about
import conference_app_2024.feature.main.generated.resources.event_map
import conference_app_2024.feature.main.generated.resources.profile_card
import conference_app_2024.feature.main.generated.resources.timetable
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.designsystem.DesignSystemRes
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.animation.FavoriteAnimationDirection
import io.github.droidkaigi.confsched.droidkaigiui.animation.ProvideFavoriteAnimation
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalAnimatedVisibilityScope
import io.github.droidkaigi.confsched.main.NavigationType.BottomNavigation
import io.github.droidkaigi.confsched.main.NavigationType.NavigationRail
import io.github.droidkaigi.confsched.main.section.GlassLikeBottomNavigation
import io.github.droidkaigi.confsched.main.section.GlassLikeNavRail
import io.github.droidkaigi.confsched.model.isBlurSupported
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

const val mainScreenRoute = "main"

fun NavGraphBuilder.mainScreen(
    windowSize: WindowSizeClass,
    mainNestedGraphStateHolder: MainNestedGraphStateHolder,
    mainNestedGraph: NavGraphBuilder.(mainNestedNavController: NavController, PaddingValues) -> Unit,
) {
    composable(mainScreenRoute) {
        CompositionLocalProvider(
            LocalAnimatedVisibilityScope provides this@composable,
        ) {
            MainScreen(
                windowSize = windowSize,
                mainNestedGraphStateHolder = mainNestedGraphStateHolder,
                mainNestedNavGraph = mainNestedGraph,
            )
        }
    }
}

interface MainNestedGraphStateHolder {
    val startDestination: String

    fun routeToTab(route: String): MainScreenTab?

    fun onTabSelected(
        mainNestedNavController: NavController,
        tab: MainScreenTab,
    )
}

enum class NavigationType {
    BottomNavigation,
    NavigationRail,
}

@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    mainNestedGraphStateHolder: MainNestedGraphStateHolder,
    mainNestedNavGraph: NavGraphBuilder.(NavController, PaddingValues) -> Unit,
    eventFlow: EventFlow<MainScreenEvent> = rememberEventFlow(),
    uiState: MainScreenUiState = mainScreenPresenter(eventFlow),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val navigationType: NavigationType =
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> BottomNavigation
            WindowWidthSizeClass.Medium -> NavigationRail
            WindowWidthSizeClass.Expanded -> NavigationRail
            else -> BottomNavigation
        }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    ProvideFavoriteAnimation(
        if (navigationType == BottomNavigation) {
            FavoriteAnimationDirection.Vertical
        } else {
            FavoriteAnimationDirection.Horizontal
        },
    ) {
        MainScreen(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            navigationType = navigationType,
            routeToTab = mainNestedGraphStateHolder::routeToTab,
            onTabSelected = mainNestedGraphStateHolder::onTabSelected,
            mainNestedNavGraph = mainNestedNavGraph,
        )
    }
}

sealed class IconRepresentation {
    data class Vector(val imageVector: ImageVector) : IconRepresentation()

    @ExperimentalResourceApi
    data class Drawable(val drawableId: DrawableResource) : IconRepresentation()
}

enum class MainScreenTab(
    val iconOff: DrawableResource,
    val iconOn: DrawableResource,
    val label: StringResource,
    val contentDescription: StringResource,
    val testTag: String = "mainScreenTab:$label",
) {
    Timetable(
        iconOff = DesignSystemRes.drawable.ic_timetable_off,
        iconOn = DesignSystemRes.drawable.ic_timetable_on,
        label = MainRes.string.timetable,
        contentDescription = MainRes.string.timetable,
    ),

    EventMap(
        iconOff = DesignSystemRes.drawable.ic_map_off,
        iconOn = DesignSystemRes.drawable.ic_map_on,
        label = MainRes.string.event_map,
        contentDescription = MainRes.string.event_map,
    ),

    Favorite(
        iconOff = DesignSystemRes.drawable.ic_fav_off,
        iconOn = DesignSystemRes.drawable.ic_fav_on,
        label = MainRes.string.event_map,
        contentDescription = MainRes.string.event_map,
    ),

    About(
        iconOff = DesignSystemRes.drawable.ic_info_off,
        iconOn = DesignSystemRes.drawable.ic_info_on,
        label = MainRes.string.about,
        contentDescription = MainRes.string.about,
    ),

    ProfileCard(
        iconOff = DesignSystemRes.drawable.ic_profilecard_off,
        iconOn = DesignSystemRes.drawable.ic_profilecard_on,
        label = MainRes.string.profile_card,
        contentDescription = MainRes.string.profile_card,
    ),
    ;

    companion object {
        val size: Int get() = entries.size
        fun indexOf(tab: MainScreenTab): Int = entries.indexOf(tab)
        fun fromIndex(index: Int): MainScreenTab = entries[index]
    }
}

data class MainScreenUiState(
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun MainScreen(
    @Suppress("UnusedParameter")
    uiState: MainScreenUiState,
    @Suppress("UnusedParameter")
    snackbarHostState: SnackbarHostState,
    navigationType: NavigationType,
    routeToTab: String.() -> MainScreenTab?,
    onTabSelected: (NavController, MainScreenTab) -> Unit,
    mainNestedNavGraph: NavGraphBuilder.(NavController, PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mainNestedNavController = rememberNavController()

    val navBackStackEntryRoute =
        mainNestedNavController.currentBackStackEntryAsState().value?.destination?.route

    // The rememberSaveable key isn't used when returning from the back stack, so we can ignore the null value of the route using this rememberSaveable.
    // This prevents unexpected animations when navigating back.
    // https://github.com/DroidKaigi/conference-app-2024/pull/732/files#r1727479543
    val lastEntryRoute = rememberSaveable(navBackStackEntryRoute) {
        navBackStackEntryRoute ?: "timetable"
    }
    val currentTab = lastEntryRoute.routeToTab() ?: MainScreenTab.Timetable

    val hazeState = remember { HazeState() }

    val scaffoldPadding = remember { mutableStateOf(PaddingValues(0.dp)) }

    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == NavigationRail) {
            GlassLikeNavRail(
                hazeState = hazeState,
                onTabSelected = {
                    onTabSelected(mainNestedNavController, it)
                },
                currentTab = currentTab,
                modifier = Modifier.padding(scaffoldPadding.value),
            )
        }

        Scaffold(
            bottomBar = {
                AnimatedVisibility(visible = navigationType == BottomNavigation) {
                    GlassLikeBottomNavigation(
                        hazeState = hazeState,
                        onTabSelected = {
                            onTabSelected(mainNestedNavController, it)
                        },
                        currentTab = currentTab,
                        modifier = Modifier.navigationBarsPadding(),
                    )
                }
            },
        ) { padding ->
            scaffoldPadding.value = padding
            val hazeStyle =
                HazeStyle(
                    tint = MaterialTheme.colorScheme.hazeTint,
                    blurRadius = 30.dp,
                )
            NavHost(
                navController = mainNestedNavController,
                startDestination = "timetable",
                modifier =
                Modifier.haze(
                    hazeState,
                    hazeStyle,
                ),
                enterTransition = { materialFadeThroughIn() },
                exitTransition = { materialFadeThroughOut() },
            ) {
                mainNestedNavGraph(mainNestedNavController, padding)
            }
        }
    }
}

private val ColorScheme.hazeTint: Color
    @Composable get() = if (isBlurSupported()) {
        scrim.copy(alpha = 0.4f)
    } else {
        scrim
    }

private fun materialFadeThroughIn(): EnterTransition =
    fadeIn(
        animationSpec =
        tween(
            durationMillis = 195,
            delayMillis = 105,
            easing = LinearOutSlowInEasing,
        ),
    ) +
        scaleIn(
            animationSpec =
            tween(
                durationMillis = 195,
                delayMillis = 105,
                easing = LinearOutSlowInEasing,
            ),
            initialScale = 0.92f,
        )

private fun materialFadeThroughOut(): ExitTransition =
    fadeOut(
        animationSpec =
        tween(
            durationMillis = 105,
            delayMillis = 0,
            easing = FastOutLinearInEasing,
        ),
    )
