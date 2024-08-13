package io.github.droidkaigi.confsched.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.droidkaigi.confsched.contributors.ContributorsScreen
import io.github.droidkaigi.confsched.contributors.contributorsScreenRoute
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.eventmap.navigateEventMapScreen
import io.github.droidkaigi.confsched.main.MainNestedGraphStateHolder
import io.github.droidkaigi.confsched.main.MainScreenTab
import io.github.droidkaigi.confsched.main.MainScreenTab.About
import io.github.droidkaigi.confsched.main.MainScreenTab.EventMap
import io.github.droidkaigi.confsched.main.MainScreenTab.Favorite
import io.github.droidkaigi.confsched.main.MainScreenTab.ProfileCard
import io.github.droidkaigi.confsched.main.MainScreenTab.Timetable
import io.github.droidkaigi.confsched.main.mainScreen
import io.github.droidkaigi.confsched.main.mainScreenRoute
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import io.github.droidkaigi.confsched.sessions.navigateTimetableScreen
import io.github.droidkaigi.confsched.sessions.navigateToSearchScreen
import io.github.droidkaigi.confsched.sessions.navigateToTimetableItemDetailScreen
import io.github.droidkaigi.confsched.sessions.nestedSessionScreens
import io.github.droidkaigi.confsched.sessions.sessionScreens
import io.github.droidkaigi.confsched.sessions.timetableScreenRoute
import io.github.droidkaigi.confsched.ui.NavHostWithSharedAxisX
import platform.UIKit.UIViewController

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Suppress("UNUSED")
fun kaigiAppController(
    repositories: Repositories,
): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalRepositories provides repositories.map
    ) {
        val windowSizeClass = calculateWindowSizeClass()
        KaigiApp(
            windowSize = windowSizeClass,
        )
    }
}

@Composable
fun KaigiApp(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    KaigiTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            KaigiNavHost(
                windowSize = windowSize,
            )
        }
    }
}

@Composable
private fun KaigiNavHost(
    windowSize: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
//    externalNavController: ExternalNavController = rememberExternalNavController(),
) {
    NavHostWithSharedAxisX(navController = navController, startDestination = mainScreenRoute) {
        mainScreen(windowSize, navController)
        sessionScreens(
            onNavigationIconClick = navController::popBackStack,
            onLinkClick = {}, //externalNavController::navigate,
            onCalendarRegistrationClick = {},//externalNavController::navigateToCalendarRegistration,
            // For debug
//            onShareClick = externalNavController::onShareClick,
            onShareClick = {
                navController.navigate(contributorsScreenRoute)
            },
        )

        // For KMP, we are not using navigation abstraction for contributors screen
        composable(contributorsScreenRoute) {
            val lifecycleOwner = LocalLifecycleOwner.current
            ContributorsScreen(
                onNavigationIconClick = {
//                    handleOnClickIfNotNavigating(
//                        lifecycleOwner,
//                        navController::popBackStack,
//                    )
                },
                onContributorsItemClick = {}, // externalNavController::navigate,
            )
        }
    }
}

private fun NavGraphBuilder.mainScreen(
    windowSize: WindowSizeClass,
    navController: NavHostController,
//    externalNavController: ExternalNavController,
) {
    mainScreen(
        windowSize = windowSize,
        mainNestedGraphStateHolder = KaigiAppMainNestedGraphStateHolder(),
        mainNestedGraph = { mainNestedNavController, contentPadding ->
            nestedSessionScreens(
                modifier = Modifier,
                onSearchClick = navController::navigateToSearchScreen,
                onTimetableItemClick = navController::navigateToTimetableItemDetailScreen,
                contentPadding = contentPadding,
            )
        },
    )
}

class KaigiAppMainNestedGraphStateHolder : MainNestedGraphStateHolder {
    override val startDestination: String = timetableScreenRoute

    override fun routeToTab(route: String): MainScreenTab? {
        return when (route) {
            timetableScreenRoute -> Timetable
            else -> null
        }
    }

    override fun onTabSelected(
        mainNestedNavController: NavController,
        tab: MainScreenTab,
    ) {
        when (tab) {
            Timetable -> mainNestedNavController.navigateTimetableScreen()
            EventMap -> mainNestedNavController.navigateEventMapScreen()
            About -> TODO()
            ProfileCard -> TODO()
            Favorite -> TODO()
        }
    }
}
