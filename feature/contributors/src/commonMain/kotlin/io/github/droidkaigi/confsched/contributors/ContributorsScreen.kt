package io.github.droidkaigi.confsched.contributors

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.Lifecycle
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.contributors.component.ContributorListItem
import io.github.droidkaigi.confsched.model.Contributor
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.rememberUserMessageStateHolder
import kotlinx.collections.immutable.PersistentList

const val contributorsScreenRoute = "contributors"
const val ContributorsScreenTestTag = "ContributorsScreenTestTag"

data class ContributorsUiState(val contributors: PersistentList<Contributor>)

@Composable
fun ContributorsScreen(
    lifecycle:Lifecycle,
    isTopAppBarHidden: Boolean = false,
    onNavigationIconClick: () -> Unit,
    onContributorItemClick: (url: String) -> Unit,
) {
    val eventEmitter = rememberEventEmitter<ContributorsScreenEvent>()
    val userMessageStateHolder = rememberUserMessageStateHolder()
    val uiState = contributorsScreenViewModel(
        events = eventEmitter,
        userMessageStateHolder = userMessageStateHolder,
    )

    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = userMessageStateHolder,
    )
    ContributorsScreen(
        uiState = uiState,
        isTopAppBarHidden = isTopAppBarHidden,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        onContributorItemClick = onContributorItemClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContributorsScreen(
    uiState: ContributorsUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onContributorItemClick: (url: String) -> Unit,
    isTopAppBarHidden: Boolean,
) {
    val scrollBehavior =
        if (!isTopAppBarHidden) {
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        } else {
            null
        }
    Scaffold(
        modifier = Modifier.testTag(ContributorsScreenTestTag),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (scrollBehavior != null) {
                LargeTopAppBar(
                    title = {
                        Text(text = "Contributor")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
    ) { padding ->
        Contributors(
            contributors = uiState.contributors,
            onContributorItemClick = onContributorItemClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
    onContributorItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(contributors) {
            ContributorListItem(
                contributor = it,
                onClick = onContributorItemClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
