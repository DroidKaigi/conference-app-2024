package io.github.droidkaigi.confsched.data.eventmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsState
import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.model.EventMapRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow

public class DefaultEventMapRepository(
    private val eventMapApi: EventMapApiClient,
) : EventMapRepository {
    private val eventMapStateFlow =
        MutableStateFlow<PersistentList<EventMapEvent>>(persistentListOf())

    @Composable
    override fun eventMapEvents(): PersistentList<EventMapEvent> {
        val eventMap by eventMapStateFlow.safeCollectAsState()
        SafeLaunchedEffect(Unit) {
            if (eventMap.isEmpty()) {
                refresh()
            }
        }
        return eventMap
    }

    override suspend fun refresh() {
        eventMapApi
            .eventMapEvents()
            .toPersistentList().also { eventMapStateFlow.value = it }
    }
}
