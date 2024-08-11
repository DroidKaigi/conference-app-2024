package io.github.droidkaigi.confsched.data.eventmap

import de.jensklingenberg.ktorfit.http.GET
import io.github.droidkaigi.confsched.data.eventmap.response.EventMapResponse
import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.model.MultiLangText
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
    val roomIdToNameMap = this.rooms.associateBy({ it.id }, { it.name.ja to it.name.en })

    return this.events
        .mapNotNull { event ->
            roomIdToNameMap[event.roomId]?.let { roomName ->
                EventMapEvent(
                    name = MultiLangText(
                        jaTitle = event.title.ja,
                        enTitle = event.title.en,
                    ),
                    roomName = MultiLangText(
                        jaTitle = roomName.first,
                        enTitle = roomName.second,
                    ),
                    description = MultiLangText(
                        jaTitle = event.i18nDesc.ja,
                        enTitle = event.i18nDesc.en,
                    ),
                )
            }
        }
        .toPersistentList()
}
