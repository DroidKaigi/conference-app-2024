package io.github.droidkaigi.confsched.eventmap

import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.ui.composeViewController
import io.github.droidkaigi.confsched.ui.presenterStateFlow
import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

@Suppress("UNUSED")
fun eventMapViewController(
    repositories: Repositories,
    onEventMapItemClick: (url: String) -> Unit,
): UIViewController = composeViewController(repositories) {
    EventMapScreen(
        isTopAppBarHidden = true,
        onNavigationIconClick = { /* no action for iOS side */ },
        onEventMapItemClick = onEventMapItemClick,
    )
}

@Suppress("unused")
fun eventMapScreenPresenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: Flow<EventMapScreenEvent>,
): Flow<EventMapUiState> = presenterStateFlow(
    events = events,
    repositories = repositories,
) {
    eventMapScreenPresenter(events)
}
