package io.github.droidkaigi.confsched.eventmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.EventMapRepository
import io.github.droidkaigi.confsched.model.localEventMapRepository

sealed interface EventMapScreenEvent

@Composable
fun eventMapScreenPresenter(
    events: EventFlow<EventMapScreenEvent>,
    eventMapRepository: EventMapRepository = localEventMapRepository(),
): EventMapUiState = providePresenterDefaults { userMessageStateHolder ->
    val eventMap by rememberUpdatedState(eventMapRepository.eventMapEvents())
    EventEffect(events) { event ->
    }

    if (eventMap.isEmpty()) return@providePresenterDefaults EventMapUiState.Loading(userMessageStateHolder)
    EventMapUiState.Exists(
        eventMap = eventMap,
        userMessageStateHolder = userMessageStateHolder,
    )
}
