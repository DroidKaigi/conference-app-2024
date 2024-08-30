package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.compose.compositionLocalProviderWithReturnValue
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

@Suppress("unused")
fun <EVENT, UISTATE> presenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: Flow<EVENT>,
    presenter: @Composable (events: Flow<EVENT>) -> UISTATE,
): Flow<UISTATE> {
    return moleculeFlow(RecompositionMode.Immediate) {
        val nestedRegistry = remember {
            object : ViewModelStoreOwner {
                override val viewModelStore: ViewModelStore = ViewModelStore()
            }
        }
        val lifecycleRegistry = remember {
            object : LifecycleOwner {
                override val lifecycle: Lifecycle = LifecycleRegistry(this)
            }
        }
        compositionLocalProviderWithReturnValue(LocalViewModelStoreOwner provides nestedRegistry) {
            compositionLocalProviderWithReturnValue(LocalLifecycleOwner provides lifecycleRegistry) {
                compositionLocalProviderWithReturnValue(LocalRepositories provides repositories) {
                    presenter(events)
                }
            }
        }
    }
}

@Suppress("UNUSED")
fun composeViewController(
    repositories: Repositories,
    content: @Composable () -> Unit,
): UIViewController = ComposeUIViewController {
    NavHost(rememberNavController(), startDestination = "root") {
        composable("root") {
            CompositionLocalProvider(
                LocalRepositories provides repositories.map,
            ) {
                Logger.d { "contributorViewController" }
                val uiViewController = LocalUIViewController.current
                LaunchedEffect(uiViewController) {
//        uiViewController
                    // TODO: How to know the destroy event of the ViewController?
//        viewModel.viewModelScope.cancel()
                }

                KaigiTheme {
                    content()
                }
            }
        }
    }
}
