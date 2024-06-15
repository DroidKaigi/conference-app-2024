package io.github.droidkaigi.confsched.data.eventmap

import de.jensklingenberg.ktorfit.http.GET
import io.github.droidkaigi.confsched.data.eventmap.response.EventMapResponse
import io.github.droidkaigi.confsched.model.EventMapEvent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal interface EventMapApi {
    @GET("/events/droidkaigi2023/eventmap")
    suspend fun getEventMap(): EventMapResponse
}

public interface EventMapApiClient {

    public suspend fun eventMapEvents(): PersistentList<EventMapEvent>
}

public fun EventMapResponse.toEventMapList(): PersistentList<EventMapEvent> {
    return listOf(EventMapEvent()).toPersistentList()
}
