package io.github.droidkaigi.confsched.contributors

import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.droidkaigiui.composeViewController
import io.github.droidkaigi.confsched.droidkaigiui.presenterStateFlow
import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

@Suppress("UNUSED")
fun contributorsViewController(
    repositories: Repositories,
    onContributorsItemClick: (url: String) -> Unit,
): UIViewController = composeViewController(repositories) {
    ContributorsScreen(
        isTopAppBarHidden = true,
        onNavigationIconClick = { /* no action for iOS side */ },
        onContributorsItemClick = onContributorsItemClick,
    )
}

@Suppress("unused")
fun contributorsScreenPresenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: EventFlow<ContributorsScreenEvent>,
): Flow<ContributorsUiState> = presenterStateFlow(
    events = events,
    repositories = repositories,
) {
    contributorsScreenPresenter(events)
}
