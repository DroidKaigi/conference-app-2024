package io.github.droidkaigi.confsched.contributors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.contributors.generated.resources.contributor_title
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.contributors.component.ContributorsCountItem
import io.github.droidkaigi.confsched.contributors.component.ContributorsItem
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.model.Contributor
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource

const val contributorsScreenRoute = "contributors"
const val ContributorsScreenTestTag = "ContributorsScreenTestTag"
const val ContributorsTestTag = "ContributorsTestTag"
const val ContributorsItemTestTagPrefix = "ContributorsItemTestTag:"
const val ContributorsTotalCountTestTag = "ContributorsTotalCountTestTag"

fun NavGraphBuilder.contributorsScreens(
    onNavigationIconClick: () -> Unit,
    onContributorItemClick: (url: String) -> Unit,
) {
    composable(contributorsScreenRoute) {
        ContributorsScreen(
            onNavigationIconClick = onNavigationIconClick,
            onContributorsItemClick = onContributorItemClick,
        )
    }
}

sealed class ContributorsUiState {
    abstract val userMessageStateHolder: UserMessageStateHolder
}

class Loading(
    override val userMessageStateHolder: UserMessageStateHolder,
) : ContributorsUiState()

class Exists(
    override val userMessageStateHolder: UserMessageStateHolder,
    val contributors: PersistentList<Contributor>,
) : ContributorsUiState()

@Composable
fun ContributorsScreen(
    onNavigationIconClick: () -> Unit,
    onContributorsItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val eventFlow = rememberEventFlow<ContributorsScreenEvent>()
    val uiState = contributorsScreenPresenter(
        events = eventFlow,
    )

    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    ContributorsScreen(
        uiState = uiState,
        isTopAppBarHidden = isTopAppBarHidden,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        onContributorsItemClick = onContributorsItemClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributorsScreen(
    uiState: ContributorsUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onContributorsItemClick: (url: String) -> Unit,
    isTopAppBarHidden: Boolean,
    modifier: Modifier = Modifier,
) {
    val layoutDirection = LocalLayoutDirection.current
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    Scaffold(
        modifier = modifier.testTag(ContributorsScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (!isTopAppBarHidden) {
                AnimatedMediumTopAppBar(
                    title = stringResource(ContributorsRes.string.contributor_title),
                    onBackClick = onBackClick,
                    scrollBehavior = scrollBehavior,
                    navIconContentDescription = "Back",
                )
            }
        },
        contentWindowInsets = WindowInsets.displayCutout.union(WindowInsets.systemBars),
    ) { padding ->
        when (uiState) {
            is Exists -> {
                Contributors(
                    contributors = uiState.contributors,
                    onContributorsItemClick = onContributorsItemClick,
                    contentPadding = PaddingValues(
                        start = padding.calculateStartPadding(layoutDirection),
                        end = padding.calculateEndPadding(layoutDirection),
                        bottom = 40.dp + padding.calculateBottomPadding(),
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding())
                        .let {
                            if (scrollBehavior != null) {
                                it.nestedScroll(scrollBehavior.nestedScrollConnection)
                            } else {
                                it
                            }
                        },
                )
            }
            is Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(padding).fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun Contributors(
    contributors: PersistentList<Contributor>,
    onContributorsItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier.testTag(ContributorsTestTag),
        contentPadding = contentPadding,
    ) {
        item {
            ContributorsCountItem(
                totalContributor = contributors.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(ContributorsTotalCountTestTag),
            )
        }
        items(contributors) {
            ContributorsItem(
                contributor = it,
                onClick = onContributorsItemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(ContributorsItemTestTagPrefix.plus(it.id)),
            )
        }
    }
}
