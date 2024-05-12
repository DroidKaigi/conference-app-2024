package io.github.droidkaigi.confsched.contributors

import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.ui.composeViewController
import io.github.droidkaigi.confsched.ui.presenterStateFlow
import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

@Suppress("UNUSED")
fun contributorViewController(
    repositories: Repositories,
    onContributorItemClick: (url: String) -> Unit,
): UIViewController = composeViewController(repositories) {
    ContributorsScreen(
        isTopAppBarHidden = true,
        onNavigationIconClick = { /** no action for iOS side **/ },
        onContributorItemClick = onContributorItemClick,
    )
}

@Suppress("unused")
fun contributorScreenPresenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: Flow<ContributorsScreenEvent>,
): Flow<ContributorsUiState> = presenterStateFlow(
    events = events,
    repositories = repositories
) {
    contributorsScreenPresenter(events)
}


