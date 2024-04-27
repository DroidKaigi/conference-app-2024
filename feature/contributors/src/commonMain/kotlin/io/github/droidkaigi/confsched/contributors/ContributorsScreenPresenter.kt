package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import io.github.droidkaigi.confsched.compose.CompositionLocalProviderWithReturnValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.model.localContributorsRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

sealed interface ContributorsScreenEvent {
}

@Suppress("unused")
fun contributorScreenPresenterStateFlow(events: Flow<ContributorsScreenEvent>): StateFlow<ContributorsUiState> {
    val coroutineScope = CoroutineScope(Job())
    return coroutineScope.launchMolecule(RecompositionMode.Immediate) {
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
                contributorsScreenPresenter(events = events)
            }
        }
    }
}

@Composable
fun contributorsScreenPresenter(
    events: Flow<ContributorsScreenEvent>,
    contributorsRepository: ContributorsRepository = localContributorsRepository(),
): ContributorsUiState = providePresenterDefaults { userMessageStateHolder ->
    val contributors by rememberUpdatedState(contributorsRepository.contributors())
    SafeLaunchedEffect(Unit) {
        events.collect {

        }
    }
    ContributorsUiState(
        contributors = contributors,
        userMessageStateHolder = userMessageStateHolder,
    )
}
