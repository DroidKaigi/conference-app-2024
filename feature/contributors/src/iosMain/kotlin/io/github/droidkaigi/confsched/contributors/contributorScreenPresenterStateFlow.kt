package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import io.github.droidkaigi.confsched.compose.CompositionLocalProviderWithReturnValue
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

@Suppress("unused")
fun contributorScreenPresenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: Flow<ContributorsScreenEvent>,
): Flow<ContributorsUiState> {
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
        CompositionLocalProviderWithReturnValue(LocalViewModelStoreOwner provides nestedRegistry) {
            CompositionLocalProviderWithReturnValue(LocalLifecycleOwner provides lifecycleRegistry) {
                CompositionLocalProviderWithReturnValue(LocalRepositories provides repositories) {
                    contributorsScreenPresenter(events = events)
                }
            }
        }
    }
}

