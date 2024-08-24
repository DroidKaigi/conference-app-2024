package io.github.droidkaigi.confsched.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.droidkaigi.confsched.about.aboutScreen
import io.github.droidkaigi.confsched.about.aboutScreenRoute
import io.github.droidkaigi.confsched.about.navigateAboutScreen
import io.github.droidkaigi.confsched.contributors.contributorsScreenRoute
import io.github.droidkaigi.confsched.contributors.contributorsScreens
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.eventmap.eventMapScreens
import io.github.droidkaigi.confsched.eventmap.navigateEventMapScreen
import io.github.droidkaigi.confsched.favorites.favoritesScreens
import io.github.droidkaigi.confsched.favorites.navigateFavoritesScreen
import io.github.droidkaigi.confsched.main.MainNestedGraphStateHolder
import io.github.droidkaigi.confsched.main.MainScreenTab
import io.github.droidkaigi.confsched.main.MainScreenTab.About
import io.github.droidkaigi.confsched.main.MainScreenTab.EventMap
import io.github.droidkaigi.confsched.main.MainScreenTab.Favorite
import io.github.droidkaigi.confsched.main.MainScreenTab.ProfileCard
import io.github.droidkaigi.confsched.main.MainScreenTab.Timetable
import io.github.droidkaigi.confsched.main.mainScreen
import io.github.droidkaigi.confsched.main.mainScreenRoute
import io.github.droidkaigi.confsched.model.AboutItem
import io.github.droidkaigi.confsched.model.Lang.JAPANESE
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import io.github.droidkaigi.confsched.model.defaultLang
import io.github.droidkaigi.confsched.profilecard.navigateProfileCardScreen
import io.github.droidkaigi.confsched.profilecard.profileCardScreen
import io.github.droidkaigi.confsched.sessions.navigateTimetableScreen
import io.github.droidkaigi.confsched.sessions.navigateToSearchScreen
import io.github.droidkaigi.confsched.sessions.navigateToTimetableItemDetailScreen
import io.github.droidkaigi.confsched.sessions.nestedSessionScreens
import io.github.droidkaigi.confsched.sessions.searchScreens
import io.github.droidkaigi.confsched.sessions.sessionScreens
import io.github.droidkaigi.confsched.sessions.timetableScreenRoute
import io.github.droidkaigi.confsched.settings.settingsScreens
import io.github.droidkaigi.confsched.shared.share.ShareNavigator
import io.github.droidkaigi.confsched.sponsors.sponsorsScreenRoute
import io.github.droidkaigi.confsched.sponsors.sponsorsScreens
import io.github.droidkaigi.confsched.staff.staffScreenRoute
import io.github.droidkaigi.confsched.staff.staffScreens
import io.github.droidkaigi.confsched.droidkaigiui.NavHostWithSharedAxisX
import io.github.droidkaigi.confsched.eventmap.eventMapScreenRoute
import io.github.droidkaigi.confsched.favorites.favoritesScreenRoute
import io.github.droidkaigi.confsched.profilecard.profileCardScreenRoute
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
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
    // If necessary, make modifications to use remember as in the KaigiApp.kt implementation.
    externalNavController: ExternalNavController = ExternalNavController(
        shareNavigator = ShareNavigator(),
    ),
) {
    NavHostWithSharedAxisX(navController = navController, startDestination = mainScreenRoute) {
        mainScreen(
            windowSize = windowSize,
            navController = navController,
            externalNavController = externalNavController,
        )
        sessionScreens(
            onNavigationIconClick = navController::popBackStack,
            onLinkClick = externalNavController::navigate,
            onCalendarRegistrationClick = {},//externalNavController::navigateToCalendarRegistration,
            // For debug
//            onShareClick = externalNavController::onShareClick,
            onShareClick = {
                navController.navigate(contributorsScreenRoute)
            },
            onFavoriteListClick = {} // { navController.navigate(favoritesScreenRoute) }
        )

        contributorsScreens(
            onNavigationIconClick = navController::popBackStack,
            onContributorItemClick = externalNavController::navigate,
        )

        searchScreens(
            onTimetableItemClick = navController::navigateToTimetableItemDetailScreen,
            onBackClick = navController::popBackStack,
        )

        staffScreens(
            onNavigationIconClick = navController::popBackStack,
            onStaffItemClick = externalNavController::navigate,
        )

        settingsScreens(
            onNavigationIconClick = navController::popBackStack,
        )

        sponsorsScreens(
            onNavigationIconClick = navController::popBackStack,
            onSponsorsItemClick = externalNavController::navigate,
        )

        favoritesScreens(
            onTimetableItemClick = navController::navigateToTimetableItemDetailScreen,
            contentPadding = PaddingValues(),
        )
    }
}

private fun NavGraphBuilder.mainScreen(
    windowSize: WindowSizeClass,
    navController: NavHostController,
    externalNavController: ExternalNavController,
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
            eventMapScreens(
                contentPadding = contentPadding,
                onEventMapItemClick = externalNavController::navigate,
            )
            favoritesScreens(
                onTimetableItemClick = navController::navigateToTimetableItemDetailScreen,
                contentPadding = contentPadding,
            )
            aboutScreen(
                contentPadding = contentPadding,
                onAboutItemClick = { aboutItem ->
                    val portalBaseUrl = if (defaultLang() == JAPANESE) {
                        "https://portal.droidkaigi.jp"
                    } else {
                        "https://portal.droidkaigi.jp/en"
                    }
                    when (aboutItem) {
                        AboutItem.Map -> externalNavController.navigate(
                            url = "https://goo.gl/maps/vv9sE19JvRjYKtSP9",
                        )

                        AboutItem.Sponsors -> navController.navigate(sponsorsScreenRoute)
                        AboutItem.CodeOfConduct -> {
                            externalNavController.navigate(
                                url = "$portalBaseUrl/about/code-of-conduct",
                            )
                        }

                        AboutItem.Contributors -> navController.navigate(contributorsScreenRoute)
                        AboutItem.License -> {} //externalNavController.navigateToLicenseScreen()
                        AboutItem.Medium -> externalNavController.navigate(
                            url = "https://medium.com/droidkaigi",
                        )

                        AboutItem.PrivacyPolicy -> {
                            externalNavController.navigate(
                                url = "$portalBaseUrl/about/privacy",
                            )
                        }

                        AboutItem.Settings -> {} //navController.navigate(settingsScreenRoute)

                        AboutItem.Staff -> navController.navigate(staffScreenRoute)
                        AboutItem.X -> externalNavController.navigate(
                            url = "https://twitter.com/DroidKaigi",
                        )

                        AboutItem.YouTube -> externalNavController.navigate(
                            url = "https://www.youtube.com/c/DroidKaigi",
                        )
                    }
                },
            )
            profileCardScreen(
                contentPadding = contentPadding,
                onClickShareProfileCard = externalNavController::onShareProfileCardClick,
            )
        },
    )
}

class KaigiAppMainNestedGraphStateHolder : MainNestedGraphStateHolder {
    override val startDestination: String = timetableScreenRoute

    override fun routeToTab(route: String): MainScreenTab? {
        return when (route) {
            timetableScreenRoute -> Timetable
            eventMapScreenRoute -> EventMap
            profileCardScreenRoute -> ProfileCard
            aboutScreenRoute -> About
            favoritesScreenRoute -> Favorite
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
            About -> mainNestedNavController.navigateAboutScreen()
            ProfileCard -> mainNestedNavController.navigateProfileCardScreen()
            Favorite -> mainNestedNavController.navigateFavoritesScreen()
        }
    }
}

private class ExternalNavController(
    private val shareNavigator: ShareNavigator,
) {
    fun navigate(url: String) {
        navigateToSafari(url = url)
    }

    private fun navigateToSafari(
        url: String,
    ) {
        val nsUrl = NSURL(string = url)
        UIApplication.sharedApplication.openURL(nsUrl)
    }

    fun onShareProfileCardClick(
        text: String,
        imageBitmap: ImageBitmap,
    ) {
        shareNavigator.shareTextWithImage(
            text = text,
            image = imageBitmap,
        )
    }
}
