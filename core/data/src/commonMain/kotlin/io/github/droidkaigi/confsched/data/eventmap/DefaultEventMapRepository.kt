package io.github.droidkaigi.confsched.data.eventmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.model.EventMapRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart

public class DefaultEventMapRepository(
    private val eventMapApi: EventMapApiClient,
) : EventMapRepository {
    private val eventMapStateFlow =
        MutableStateFlow<PersistentList<EventMapEvent>>(persistentListOf())

    @Composable
    override fun eventMapEvents(): PersistentList<EventMapEvent> {
        val eventMap by eventMapStateFlow.safeCollectAsRetainedState()
        SafeLaunchedEffect(Unit) {
            if (eventMap.isEmpty()) {
                refresh()
            }
        }
        return eventMap
    }

    override fun getEventMapStream(): Flow<PersistentList<EventMapEvent>> {
        return eventMapStateFlow.onStart {
            if (eventMapStateFlow.value.isEmpty()) {
                refresh()
            }
        }
    }

    override suspend fun refresh() {
        eventMapApi
            .eventMapEvents()
            .toPersistentList().also { eventMapStateFlow.value = it }
    }
}
