package io.github.droidkaigi.confsched.sponsors

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Plan.GOLD
import io.github.droidkaigi.confsched.model.Plan.PLATINUM
import io.github.droidkaigi.confsched.model.Plan.SUPPORTER
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.sponsors.section.SponsorsList
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.ui.tooling.preview.Preview

const val sponsorsScreenRoute = "sponsors"
const val SponsorsScreenTestTag = "SponsorsScreenTestTag"

fun NavGraphBuilder.sponsorsScreens(
    onNavigationIconClick: () -> Unit,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    composable(sponsorsScreenRoute) {
        val lifecycleOwner = LocalLifecycleOwner.current

        SponsorsScreen(
            onNavigationIconClick = {
                handleOnClickIfNotNavigating(
                    lifecycleOwner,
                    onNavigationIconClick,
                )
            },
            onSponsorsItemClick = onSponsorsItemClick,
        )
    }
}

data class SponsorsScreenUiState(
    val sponsorsListUiState: SponsorsListUiState,
    val userMessageStateHolder: UserMessageStateHolder,
)

data class SponsorsListUiState(
    val platinumSponsors: PersistentList<Sponsor>,
    val goldSponsors: PersistentList<Sponsor>,
    val supporters: PersistentList<Sponsor>,
)

@Composable
fun SponsorsScreen(
    onNavigationIconClick: () -> Unit,
    onSponsorsItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val uiState = SponsorsScreenUiState(
        sponsorsListUiState = SponsorsListUiState(
            platinumSponsors = Sponsor.fakes().filter { it.plan == PLATINUM }.toPersistentList(),
            goldSponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
            supporters = Sponsor.fakes().filter { it.plan == SUPPORTER }.toPersistentList(),
        ),
        userMessageStateHolder = UserMessageStateHolderImpl(),
    )

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
        modifier = modifier.testTag(SponsorsScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (scrollBehavior != null) {
                LargeTopAppBar(
                    title = {
                        Text(text = "Sponsors")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                        ) {
                            Icon(
                                imageVector = Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
    ) { padding ->
        SponsorsList(
            modifier = Modifier.fillMaxSize(),
            padding = padding,
            uiState = uiState.sponsorsListUiState,
            scrollBehavior = scrollBehavior,
            onSponsorsItemClick = onSponsorsItemClick,
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
                        platinumSponsors = Sponsor.fakes().filter { it.plan == PLATINUM }.toPersistentList(),
                        goldSponsors = Sponsor.fakes().filter { it.plan == GOLD }.toPersistentList(),
                        supporters = Sponsor.fakes().filter { it.plan == SUPPORTER }.toPersistentList(),
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
