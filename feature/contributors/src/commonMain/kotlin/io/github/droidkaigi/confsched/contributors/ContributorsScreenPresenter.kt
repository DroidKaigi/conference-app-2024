package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.model.localContributorsRepository

sealed interface ContributorsScreenEvent

@Composable
fun contributorsScreenPresenter(
    events: EventFlow<ContributorsScreenEvent>,
    contributorsRepository: ContributorsRepository = localContributorsRepository(),
): ContributorsUiState = providePresenterDefaults { userMessageStateHolder ->
    val contributors by rememberUpdatedState(contributorsRepository.contributors())
    EventEffect(events) { event ->
    }
    if (contributors.isEmpty()) return@providePresenterDefaults Loading(userMessageStateHolder)
    Exists(
        contributors = contributors,
        userMessageStateHolder = userMessageStateHolder,
    )
}
