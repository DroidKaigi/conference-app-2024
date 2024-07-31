package io.github.droidkaigi.confsched.sponsors

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import kotlinx.collections.immutable.PersistentList
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
    val sponsors: PersistentList<Sponsor>,
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun SponsorsScreen(
    onNavigationIconClick: () -> Unit,
    onSponsorsItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val uiState = SponsorsScreenUiState(
        sponsors = Sponsor.fakes(),
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
        LazyColumn(
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
        ) {
        }
    }
}

@Composable
@Preview
fun SponsorsScreenPreview() {
    KaigiTheme {
        Surface {
            SponsorsScreen(
                uiState = SponsorsScreenUiState(
                    sponsors = Sponsor.fakes(),
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
