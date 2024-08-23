package io.github.droidkaigi.confsched.eventmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.EventMapRepository
import io.github.droidkaigi.confsched.model.localEventMapRepository
import kotlinx.coroutines.flow.Flow

sealed interface EventMapScreenEvent

@Composable
fun eventMapScreenPresenter(
    events: Flow<EventMapScreenEvent>,
    eventMapRepository: EventMapRepository = localEventMapRepository(),
): EventMapUiState = providePresenterDefaults { userMessageStateHolder ->
    val eventMap by rememberUpdatedState(eventMapRepository.eventMapEvents())
    SafeLaunchedEffect(Unit) {
        events.collect {
        }
    }
    EventMapUiState(
        eventMap = eventMap,
        userMessageStateHolder = userMessageStateHolder,
    )
}
