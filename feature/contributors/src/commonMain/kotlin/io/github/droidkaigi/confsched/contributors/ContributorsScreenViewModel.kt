package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.model.localContributorsRepository
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import kotlinx.coroutines.flow.Flow

sealed interface ContributorsScreenEvent {
}

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
