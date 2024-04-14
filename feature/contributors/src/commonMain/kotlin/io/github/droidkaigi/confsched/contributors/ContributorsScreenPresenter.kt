package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.model.localContributorsRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface ContributorsScreenEvent {
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
