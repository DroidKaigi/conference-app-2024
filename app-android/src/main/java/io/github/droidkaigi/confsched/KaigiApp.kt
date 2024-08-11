package io.github.droidkaigi.confsched

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import co.touchlab.kermit.Logger
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.github.droidkaigi.confsched.about.aboutScreen
import io.github.droidkaigi.confsched.about.aboutScreenRoute
import io.github.droidkaigi.confsched.about.navigateAboutScreen
import io.github.droidkaigi.confsched.contributors.contributorsScreenRoute
import io.github.droidkaigi.confsched.contributors.contributorsScreens
import io.github.droidkaigi.confsched.designsystem.theme.ColorContrast
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.eventmap.eventMapScreens
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
import io.github.droidkaigi.confsched.model.AboutItem
import io.github.droidkaigi.confsched.model.Lang.JAPANESE
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.defaultLang
import io.github.droidkaigi.confsched.profilecard.navigateProfileCardScreen
import io.github.droidkaigi.confsched.profilecard.profileCardScreen
import io.github.droidkaigi.confsched.profilecard.profileCardScreenRoute
import io.github.droidkaigi.confsched.sessions.navigateTimetableScreen
import io.github.droidkaigi.confsched.sessions.navigateToTimetableItemDetailScreen
import io.github.droidkaigi.confsched.sessions.nestedSessionScreens
import io.github.droidkaigi.confsched.sessions.sessionScreens
import io.github.droidkaigi.confsched.sessions.timetableScreenRoute
import io.github.droidkaigi.confsched.share.ShareNavigator
import io.github.droidkaigi.confsched.sponsors.sponsorsScreenRoute
import io.github.droidkaigi.confsched.sponsors.sponsorsScreens
import io.github.droidkaigi.confsched.staff.staffScreenRoute
import io.github.droidkaigi.confsched.staff.staffScreens
import io.github.droidkaigi.confsched.ui.NavHostWithSharedAxisX
import kotlinx.collections.immutable.PersistentList

@Composable
fun KaigiApp(
    windowSize: WindowSizeClass,
    displayFeatures: PersistentList<DisplayFeature>,
    modifier: Modifier = Modifier,
) {
    KaigiTheme(
        colorContrast = colorContrast(),
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            KaigiNavHost(
                windowSize = windowSize,
                displayFeatures = displayFeatures,
            )
        }
    }
}

@Composable
private fun KaigiNavHost(
    windowSize: WindowSizeClass,
    @Suppress("UnusedParameter")
    displayFeatures: PersistentList<DisplayFeature>,
    navController: NavHostController = rememberNavController(),
    externalNavController: ExternalNavController = rememberExternalNavController(),
) {
    NavHostWithSharedAxisX(navController = navController, startDestination = mainScreenRoute) {
        mainScreen(windowSize, navController, externalNavController)
        sessionScreens(
            onNavigationIconClick = navController::popBackStack,
            onLinkClick = externalNavController::navigate,
            onCalendarRegistrationClick = externalNavController::navigateToCalendarRegistration,
            // For debug
//            onShareClick = externalNavController::onShareClick,
            onShareClick = {
                navController.navigate(contributorsScreenRoute)
            },
        )

        contributorsScreens(
            onNavigationIconClick = navController::popBackStack,
            onContributorItemClick = externalNavController::navigate,
        )

        staffScreens(
            onNavigationIconClick = navController::popBackStack,
            onStaffItemClick = externalNavController::navigate,
        )

        sponsorsScreens(
            onNavigationIconClick = navController::popBackStack,
            onSponsorsItemClick = externalNavController::navigate,
        )
    }
}

private fun NavGraphBuilder.mainScreen(
    windowSize: WindowSizeClass,
    navController: NavHostController,
    @Suppress("UnusedParameter")
    externalNavController: ExternalNavController,
) {
    mainScreen(
        windowSize = windowSize,
        mainNestedGraphStateHolder = KaigiAppMainNestedGraphStateHolder(),
        mainNestedGraph = { mainNestedNavController, contentPadding ->
            nestedSessionScreens(
                modifier = Modifier,
                onTimetableItemClick = navController::navigateToTimetableItemDetailScreen,
                contentPadding = contentPadding,
            )
            eventMapScreens(
                onNavigationIconClick = navController::popBackStack,
                onEventMapItemClick = externalNavController::navigate,
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
                        AboutItem.License -> externalNavController.navigateToLicenseScreen()
                        AboutItem.Medium -> externalNavController.navigate(
                            url = "https://medium.com/droidkaigi",
                        )

                        AboutItem.PrivacyPolicy -> {
                            externalNavController.navigate(
                                url = "$portalBaseUrl/about/privacy",
                            )
                        }

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
            profileCardScreen(contentPadding)
        },
    )
}

class KaigiAppMainNestedGraphStateHolder : MainNestedGraphStateHolder {
    override val startDestination: String = timetableScreenRoute

    override fun routeToTab(route: String): MainScreenTab? {
        return when (route) {
            timetableScreenRoute -> Timetable
            profileCardScreenRoute -> ProfileCard
            aboutScreenRoute -> About
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
            Favorite -> {}
            About -> mainNestedNavController.navigateAboutScreen()
            ProfileCard -> mainNestedNavController.navigateProfileCardScreen()
        }
    }
}

@Composable
private fun rememberExternalNavController(): ExternalNavController {
    val context = LocalContext.current
    val shareNavigator = ShareNavigator(context)

    return remember(context) {
        ExternalNavController(
            context = context,
            shareNavigator = shareNavigator,
        )
    }
}

private class ExternalNavController(
    private val context: Context,
    private val shareNavigator: ShareNavigator,
) {
    fun navigate(url: String) {
        val uri: Uri = url.toUri()
        val launched = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            navigateToNativeAppApi30(context = context, uri = uri)
        } else {
            navigateToNativeApp(context = context, uri = uri)
        }
        if (launched.not()) {
            navigateToCustomTab(context = context, uri = uri)
        }
    }

    /**
     * Navigate to Calendar Registration
     */
    fun navigateToCalendarRegistration(timetableItem: TimetableItem) {
        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtras(
                bundleOf(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME to timetableItem.startsAt.toEpochMilliseconds(),
                    CalendarContract.EXTRA_EVENT_END_TIME to timetableItem.endsAt.toEpochMilliseconds(),
                    CalendarContract.Events.TITLE to "[${timetableItem.room.name.currentLangTitle}] ${timetableItem.title.currentLangTitle}",
                    CalendarContract.Events.DESCRIPTION to timetableItem.url,
                    CalendarContract.Events.EVENT_LOCATION to timetableItem.room.name.currentLangTitle,
                ),
            )
        }

        runCatching {
            context.startActivity(calendarIntent)
        }.onFailure {
            Logger.e("Fail startActivity in navigateToCalendarRegistration", it)
        }
    }

    fun navigateToLicenseScreen() {
        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
    }

    fun onShareClick(timetableItem: TimetableItem) {
        shareNavigator.share(
            "[${timetableItem.room.name.currentLangTitle}] ${timetableItem.startsTimeString} - ${timetableItem.endsTimeString}\n" +
                "${timetableItem.title.currentLangTitle}\n" +
                timetableItem.url,
        )
    }

    @Suppress("SwallowedException")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun navigateToNativeAppApi30(
        context: Context,
        uri: Uri,
    ): Boolean {
        val nativeAppIntent =
            Intent(Intent.ACTION_VIEW, uri)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
        return try {
            context.startActivity(nativeAppIntent)
            true
        } catch (ex: ActivityNotFoundException) {
            false
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun navigateToNativeApp(
        context: Context,
        uri: Uri,
    ): Boolean {
        val pm = context.packageManager

        // Get all Apps that resolve a generic url
        val browserActivityIntent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.fromParts("http", "", null))
        val genericResolvedList: Set<String> =
            pm.queryIntentActivities(browserActivityIntent, 0)
                .map { it.activityInfo.packageName }
                .toSet()

        // Get all apps that resolve the specific Url
        val specializedActivityIntent = Intent(Intent.ACTION_VIEW, uri)
            .addCategory(Intent.CATEGORY_BROWSABLE)
        val resolvedSpecializedList: MutableSet<String> =
            pm.queryIntentActivities(browserActivityIntent, 0)
                .map { it.activityInfo.packageName }
                .toMutableSet()

        // Keep only the Urls that resolve the specific, but not the generic urls.
        resolvedSpecializedList.removeAll(genericResolvedList)

        // If the list is empty, no native app handlers were found.
        if (resolvedSpecializedList.isEmpty()) {
            return false
        }

        // We found native handlers. Launch the Intent.
        specializedActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(specializedActivityIntent)
        return true
    }

    private fun navigateToCustomTab(
        context: Context,
        uri: Uri,
    ) {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
            .launchUrl(context, uri)
    }
}

@Composable
@ReadOnlyComposable
private fun colorContrast(): ColorContrast {
    val uiModeManager =
        LocalContext.current.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        when (uiModeManager.contrast) {
            in 0.0f..0.33f -> ColorContrast.Default
            in 0.34f..0.66f -> ColorContrast.Medium
            in 0.67f..1.0f -> ColorContrast.High
            else -> ColorContrast.Default
        }
    } else {
        ColorContrast.Default
    }
}
