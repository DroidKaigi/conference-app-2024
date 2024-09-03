package io.github.droidkaigi.confsched.sponsors

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.sponsors.generated.resources.content_description_back
import conference_app_2024.feature.sponsors.generated.resources.sponsor
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.sponsors.section.SponsorsList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val sponsorsScreenRoute = "sponsors"

fun NavGraphBuilder.sponsorsScreens(
    onNavigationIconClick: () -> Unit,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    composable(sponsorsScreenRoute) {
        SponsorsScreen(
            onNavigationIconClick = dropUnlessResumed(block = onNavigationIconClick),
            onSponsorsItemClick = onSponsorsItemClick,
        )
    }
}

data class SponsorsScreenUiState(
    val sponsorsListUiState: SponsorsListUiState,
    val userMessageStateHolder: UserMessageStateHolder,
)

data class SponsorsListUiState(
    val platinumSponsorsUiState: PlatinumSponsorsUiState,
    val goldSponsorsUiState: GoldSponsorsUiState,
    val supportersUiState: SupportersUiState,
)

sealed interface PlatinumSponsorsUiState {
    val userMessageStateHolder: UserMessageStateHolder

    data class Loading(
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : PlatinumSponsorsUiState

    data class Exists(
        override val userMessageStateHolder: UserMessageStateHolder,
        val platinumSponsors: PersistentList<Sponsor>,
    ) : PlatinumSponsorsUiState
}

sealed interface GoldSponsorsUiState {
    val userMessageStateHolder: UserMessageStateHolder

    data class Loading(
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : GoldSponsorsUiState

    data class Exists(
        override val userMessageStateHolder: UserMessageStateHolder,
        val goldSponsors: PersistentList<Sponsor>,
    ) : GoldSponsorsUiState
}

sealed interface SupportersUiState {
    val userMessageStateHolder: UserMessageStateHolder

    data class Loading(
        override val userMessageStateHolder: UserMessageStateHolder,
    ) : SupportersUiState

    data class Exists(
        override val userMessageStateHolder: UserMessageStateHolder,
        val supporters: PersistentList<Sponsor>,
    ) : SupportersUiState
}

@Composable
fun SponsorsScreen(
    onNavigationIconClick: () -> Unit,
    onSponsorsItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val eventFlow = rememberEventFlow<SponsorsScreenEvent>()
    val uiState = sponsorsScreenPresenter(events = eventFlow)

    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    SponsorsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        onSponsorsItemClick = onSponsorsItemClick,
        modifier = modifier,
        isTopAppBarHidden = isTopAppBarHidden,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SponsorsScreen(
    uiState: SponsorsScreenUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    isTopAppBarHidden: Boolean,
    modifier: Modifier = Modifier,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (!isTopAppBarHidden) {
                AnimatedMediumTopAppBar(
                    title = stringResource(SponsorsRes.string.sponsor),
                    onBackClick = onBackClick,
                    scrollBehavior = scrollBehavior,
                    navIconContentDescription = stringResource(SponsorsRes.string.content_description_back),
                )
            }
        },
    ) { padding ->
        SponsorsList(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize(),
            uiState = uiState.sponsorsListUiState,
            scrollBehavior = scrollBehavior,
            onSponsorsItemClick = onSponsorsItemClick,
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding()),
        )
    }
}

@Composable
@Preview
fun SponsorsScreenPreview() {
    KaigiTheme {
        Surface {
            SponsorsScreen(
                uiState = SponsorsScreenUiState(
                    sponsorsListUiState = SponsorsListUiState(
                        platinumSponsorsUiState = PlatinumSponsorsUiState.Exists(
                            userMessageStateHolder = UserMessageStateHolderImpl(),
                            platinumSponsors = Sponsor.fakes().filter { it.plan == PLATINUM }.toPersistentList(),
                        ),
                        goldSponsorsUiState = GoldSponsorsUiState.Exists(
                            userMessageStateHolder = UserMessageStateHolderImpl(),
                            goldSponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
                        ),
                        supportersUiState = SupportersUiState.Exists(
                            userMessageStateHolder = UserMessageStateHolderImpl(),
                            supporters = Sponsor.fakes().filter { it.plan == SUPPORTER }.toPersistentList(),
                        ),
                    ),
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onSponsorsItemClick = {},
                onBackClick = {},
                isTopAppBarHidden = false,
            )
        }
    }
}
