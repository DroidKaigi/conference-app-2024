package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.model.localContributorsRepository
import io.github.droidkaigi.confsched.ui.ComposeViewModel
import io.github.droidkaigi.confsched.ui.DefaultComposeViewModel
import io.github.droidkaigi.confsched.ui.KmpViewModelLifecycle
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

sealed interface ContributorsScreenEvent {

}

open class ContributorsScreenViewModel(
    private val contributorsRepository: ContributorsRepository,
    composeCoroutineContext:CoroutineContext,
    private val viewModelLifecycle: KmpViewModelLifecycle,
) : ViewModel(),
    ComposeViewModel<ContributorsScreenEvent, ContributorsUiState> by DefaultComposeViewModel(
        viewModelLifecycle = viewModelLifecycle,
        composeCoroutineContext = composeCoroutineContext,
        content = { events ->
            contributorsScreenViewModel(
                events = events,
                userMessageStateHolder = this,
                contributorsRepository = contributorsRepository,
            )
        },
    )

@Composable
fun contributorsScreenViewModel(
    events: Flow<ContributorsScreenEvent>,
    userMessageStateHolder: UserMessageStateHolder,
    contributorsRepository: ContributorsRepository = localContributorsRepository(),
): ContributorsUiState {
    val contributors by rememberUpdatedState(contributorsRepository.contributors())
    SafeLaunchedEffect(Unit) {
        events.collect{

        }
    }
    return ContributorsUiState(
        contributors = contributors,
    )
}
