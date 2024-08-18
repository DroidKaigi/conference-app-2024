package io.github.droidkaigi.confsched.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.UserMessageStateHolderImpl
import io.github.droidkaigi.confsched.ui.component.AnimatedLargeTopAppBar
import io.github.droidkaigi.confsched.ui.handleOnClickIfNotNavigating
import org.jetbrains.compose.ui.tooling.preview.Preview

const val settingsScreenRoute = "settings"

fun NavGraphBuilder.settingsScreens(
    onNavigationIconClick: () -> Unit,
) {
    composable(settingsScreenRoute) {
        val lifecycleOwner = LocalLifecycleOwner.current

        SettingsScreen(
            onNavigationIconClick = {
                handleOnClickIfNotNavigating(
                    lifecycleOwner,
                    onNavigationIconClick,
                )
            },
        )
    }
}

data class SettingsUiState(
    val userMessageStateHolder: UserMessageStateHolder,
)

@Composable
fun SettingsScreen(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    isTopAppBarHidden: Boolean = false,
) {
    val eventEmitter = rememberEventEmitter<SettingsScreenEvent>()
    val uiState = settingsScreenPresenter(events = eventEmitter)

    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = uiState.userMessageStateHolder,
    )
    SettingsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigationIconClick,
        modifier = modifier,
        isTopAppBarHidden = isTopAppBarHidden,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
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
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (!isTopAppBarHidden) {
                AnimatedLargeTopAppBar(
                    title = "Settings",
                    onBackClick = onBackClick,
                    scrollBehavior = scrollBehavior,
                    navIconContentDescription = "Back",
                )
            }
        },
    ) { padding ->
        // TODO
    }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    KaigiTheme {
        Surface {
            SettingsScreen(
                uiState = SettingsUiState(
                    userMessageStateHolder = UserMessageStateHolderImpl(),
                ),
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                isTopAppBarHidden = false,
            )
        }
    }
}
