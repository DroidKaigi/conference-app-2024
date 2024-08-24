package io.github.droidkaigi.confsched.eventmap

import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.droidkaigiui.composeViewController
import io.github.droidkaigi.confsched.droidkaigiui.presenterStateFlow
import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

@Suppress("UNUSED")
fun eventMapViewController(
    repositories: Repositories,
    onEventMapItemClick: (url: String) -> Unit,
): UIViewController = composeViewController(repositories) {
    EventMapScreen(
        onEventMapItemClick = onEventMapItemClick,
    )
}

@Suppress("unused")
fun eventMapScreenPresenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: EventFlow<EventMapScreenEvent>,
): Flow<EventMapUiState> = presenterStateFlow(
    events = events,
    repositories = repositories,
) {
    eventMapScreenPresenter(events)
}
