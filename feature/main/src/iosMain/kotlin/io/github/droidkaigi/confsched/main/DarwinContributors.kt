package io.github.droidkaigi.confsched.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.compose.rememberEventEmitter
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import io.github.droidkaigi.confsched.ui.SnackbarMessageEffect
import platform.UIKit.UIViewController

@Suppress("UNUSED")
fun mainViewController(
    repositories: Repositories,
    onContributorItemClick: (url: String) -> Unit,
): UIViewController = ComposeUIViewController {
    NavHost(rememberNavController(), startDestination = "root") {
        composable("root") {
            CompositionLocalProvider(
                LocalRepositories provides repositories.map
            ) {
                Logger.d { "mainViewController" }
                val eventEmitter = rememberEventEmitter<MainScreenEvent>()

                val uiState = mainScreenPresenter(
                    events = eventEmitter,
                )

                val snackbarHostState = remember { SnackbarHostState() }

                SnackbarMessageEffect(
                    snackbarHostState = snackbarHostState,
                    userMessageStateHolder = uiState.userMessageStateHolder,
                )
                val uiViewController = LocalUIViewController.current
                LaunchedEffect(uiViewController) {
//        uiViewController
                    // TODO: How to know the destroy event of the ViewController?
//        viewModel.viewModelScope.cancel()
                }
                val mainNestedGraphStateHolder =

                KaigiTheme {

//                    MainScreen(
//                        uiState = uiState,
//                        snackbarHostState = snackbarHostState,
//                    )
                }
            }
        }
    }
}
