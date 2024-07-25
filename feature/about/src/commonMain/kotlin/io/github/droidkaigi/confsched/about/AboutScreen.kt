package io.github.droidkaigi.confsched.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
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
import io.github.droidkaigi.confsched.about.section.AboutDroidKaigiDetail
import io.github.droidkaigi.confsched.about.section.AboutFooterLinks
import io.github.droidkaigi.confsched.about.section.aboutCredits
import io.github.droidkaigi.confsched.about.section.aboutOthers
import io.github.droidkaigi.confsched.model.AboutItem
import io.github.droidkaigi.confsched.model.AboutItem.Medium
import io.github.droidkaigi.confsched.model.AboutItem.X
import io.github.droidkaigi.confsched.model.AboutItem.YouTube

const val aboutScreenRoute = "about"

object AboutTestTag {
    private const val suffix = "TestTag"
    private const val prefix = "ProfileCard"

    object DetailSection {
        private const val detailSectionPrefix = "${prefix}_DetailSection"
        const val Section = "${detailSectionPrefix}_$suffix"
    }

    object CreditsSection {
        private const val creditsSectionPrefix = "${prefix}_CreditsSection"
        const val Section = "${creditsSectionPrefix}_$suffix"
    }

    object OthersSection {
        private const val othersSectionPrefix = "${prefix}_OthersSection"
        const val Section = "${othersSectionPrefix}_$suffix"
    }

    object FooterLinksSection {
        private const val footerLinksSectionPrefix = "${prefix}_FooterLinksSection"
        const val Section = "${footerLinksSectionPrefix}_$suffix"
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onAboutItemClick: (AboutItem) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = modifier,
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
        ) {
            item {
                AboutDroidKaigiDetail()
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
                modifier = Modifier.testTag(AboutTestTag.CreditsSection.Section),
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
                modifier = Modifier.testTag(AboutTestTag.OthersSection.Section),
            )
            item {
                AboutFooterLinks(
                    // TODO: Inject the right version name
                    versionName = "1.6.0",
                    onYouTubeClick = {
                        onAboutItemClick(YouTube)
                    },
                    onXClick = {
                        onAboutItemClick(X)
                    },
                    onMediumClick = {
                        onAboutItemClick(Medium)
                    },
                    modifier = Modifier.testTag(AboutTestTag.FooterLinksSection.Section),
                )
            }
        }
    }
}
