package io.github.droidkaigi.confsched.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.about.generated.resources.about_droidkaigi
import io.github.droidkaigi.confsched.about.section.AboutDroidKaigiDetail
import io.github.droidkaigi.confsched.about.section.AboutFooterLinks
import io.github.droidkaigi.confsched.about.section.aboutCredits
import io.github.droidkaigi.confsched.about.section.aboutOthers
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.model.AboutItem
import io.github.droidkaigi.confsched.model.AboutItem.Medium
import io.github.droidkaigi.confsched.model.AboutItem.X
import io.github.droidkaigi.confsched.model.AboutItem.YouTube
import org.jetbrains.compose.resources.stringResource

const val aboutScreenRoute = "about"

object AboutScreenTestTag {
    const val Screen = "AboutScreen"
}

fun NavGraphBuilder.aboutScreen(
    contentPadding: PaddingValues,
    onAboutItemClick: (AboutItem) -> Unit,
) {
    composable(aboutScreenRoute) {
        AboutScreen(
            contentPadding = contentPadding,
            onAboutItemClick = onAboutItemClick,
        )
    }
}

fun NavController.navigateAboutScreen() {
    navigate(aboutScreenRoute) {
        popUpTo(checkNotNull(graph.findStartDestination().route)) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

data class AboutUiState(
    val versionName: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onAboutItemClick: (AboutItem) -> Unit,
) {
    val uiState = aboutScreenPresenter()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val layoutDirection = LocalLayoutDirection.current
    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = modifier.testTag(AboutScreenTestTag.Screen),
        topBar = {
            AnimatedTextTopAppBar(
                title = stringResource(AboutRes.string.about_droidkaigi),
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(
            left = contentPadding.calculateLeftPadding(layoutDirection),
            top = contentPadding.calculateTopPadding(),
            right = contentPadding.calculateRightPadding(layoutDirection),
            bottom = contentPadding.calculateBottomPadding(),
        ),
    ) { padding ->
        LazyColumn(
            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = padding,
            state = lazyListState,
        ) {
            item {
                AboutDroidKaigiDetail(
                    screenScrollState = lazyListState,
                    onViewMapClick = {
                        onAboutItemClick(AboutItem.Map)
                    },
                )
            }
            aboutCredits(
                onSponsorsItemClick = {
                    onAboutItemClick(AboutItem.Sponsors)
                },
                onContributorsItemClick = {
                    onAboutItemClick(AboutItem.Contributors)
                },
                onStaffItemClick = {
                    onAboutItemClick(AboutItem.Staff)
                },
            )
            aboutOthers(
                onCodeOfConductItemClick = {
                    onAboutItemClick(AboutItem.CodeOfConduct)
                },
                onLicenseItemClick = {
                    onAboutItemClick(AboutItem.License)
                },
                onPrivacyPolicyItemClick = {
                    onAboutItemClick(AboutItem.PrivacyPolicy)
                },
                onSettingsItemClick = {
                    onAboutItemClick(AboutItem.Settings)
                },
            )
            item {
                AboutFooterLinks(
                    versionName = uiState.versionName,
                    onYouTubeClick = {
                        onAboutItemClick(YouTube)
                    },
                    onXClick = {
                        onAboutItemClick(X)
                    },
                    onMediumClick = {
                        onAboutItemClick(Medium)
                    },
                )
            }
        }
    }
}
