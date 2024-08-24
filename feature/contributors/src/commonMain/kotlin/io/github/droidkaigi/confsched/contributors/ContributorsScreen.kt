package io.github.droidkaigi.confsched.contributors

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import conference_app_2024.feature.contributors.generated.resources.contributor_title
import io.github.droidkaigi.confsched.compose.rememberEventFlow
import io.github.droidkaigi.confsched.contributors.component.ContributorsItem
import io.github.droidkaigi.confsched.droidkaigiui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageStateHolder
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedLargeTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.handleOnClickIfNotNavigating
import io.github.droidkaigi.confsched.model.Contributor
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource

const val contributorsScreenRoute = "contributors"
const val ContributorsScreenTestTag = "ContributorsScreenTestTag"
const val ContributorsTestTag = "ContributorsTestTag"
const val ContributorsItemTestTagPrefix = "ContributorsItemTestTag:"

fun NavGraphBuilder.contributorsScreens(
    onNavigationIconClick: () -> Unit,
    onContributorItemClick: (url: String) -> Unit,
) {
    composable(contributorsScreenRoute) {
        val lifecycleOwner = LocalLifecycleOwner.current
        ContributorsScreen(
            onNavigationIconClick = {
                handleOnClickIfNotNavigating(
                    lifecycleOwner,
                    onNavigationIconClick,
                )
            },
            onContributorsItemClick = onContributorItemClick,
        )
    }
}

data class ContributorsUiState(
    val contributors: PersistentList<Contributor>,
    val userMessageStateHolder: UserMessageStateHolder,
)

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
                AnimatedLargeTopAppBar(
                    title = stringResource(ContributorsRes.string.contributor_title),
                    onBackClick = onBackClick,
                    scrollBehavior = scrollBehavior,
                    navIconContentDescription = "Back",
                )
            }
        },
    ) { padding ->
        Contributors(
            contributors = uiState.contributors,
            onContributorsItemClick = onContributorsItemClick,
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding()),
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
