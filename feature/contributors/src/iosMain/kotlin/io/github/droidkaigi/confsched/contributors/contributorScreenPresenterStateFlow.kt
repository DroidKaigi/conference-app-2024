package io.github.droidkaigi.confsched.contributors

import io.github.droidkaigi.confsched.ui.presenterStateFlow
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

@Suppress("unused")
fun contributorScreenPresenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: Flow<ContributorsScreenEvent>,
): Flow<ContributorsUiState> {
    return presenterStateFlow(
        events = events,
        repositories = repositories,
        presenter = {
            contributorsScreenPresenter(events)
        }
    )
}

