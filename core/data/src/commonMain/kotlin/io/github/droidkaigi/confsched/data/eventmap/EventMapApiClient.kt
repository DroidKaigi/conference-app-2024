package io.github.droidkaigi.confsched.data.eventmap

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import io.github.droidkaigi.confsched.data.NetworkService
import io.github.droidkaigi.confsched.data.eventmap.response.EventMapResponse
import io.github.droidkaigi.confsched.data.eventmap.response.MessageResponse
import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.model.MultiLangText
import io.github.droidkaigi.confsched.model.RoomIcon
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal interface EventMapApi {
    /**
     * Gets event (project) and room information for DroidKaigi 2024 event.
     *
     * @return [EventMapResponse]
     */
    @GET("/events/droidkaigi2024/projects")
    suspend fun getEventMap(): EventMapResponse
}

public class DefaultEventMapApiClient(
    private val networkService: NetworkService,
    ktorfit: Ktorfit,
) : EventMapApiClient {

    private val eventMapApi = ktorfit.create<EventMapApi>()

    public override suspend fun eventMapEvents(): PersistentList<EventMapEvent> {
        return networkService {
            eventMapApi.getEventMap()
        }.toEventMapList()
    }
}

public interface EventMapApiClient {

    public suspend fun eventMapEvents(): PersistentList<EventMapEvent>
}

public fun EventMapResponse.toEventMapList(): PersistentList<EventMapEvent> {
    val roomIdToNameMap = this.rooms.associateBy({ it.id }, { it.name.ja to it.name.en })

    return this.projects
        .mapNotNull { project ->
            roomIdToNameMap[project.roomId]?.let { roomName ->
                EventMapEvent(
                    name = MultiLangText(
                        jaTitle = project.title.ja,
                        enTitle = project.title.en,
                    ),
                    roomName = MultiLangText(
                        jaTitle = roomName.first,
                        enTitle = roomName.second,
                    ),
                    roomIcon = roomName.second.toRoomIcon(),
                    description = MultiLangText(
                        jaTitle = project.i18nDesc.ja,
                        enTitle = project.i18nDesc.en,
                    ),
                    moreDetailsUrl = project.moreDetailsUrl,
                    message = project.message?.toMultiLangText(),
                )
            }
        }
        .toPersistentList()
}

private fun String.toRoomIcon(): RoomIcon = when (this) {
    "Iguana" -> RoomIcon.Square
    "Hedgehog" -> RoomIcon.Diamond
    "Giraffe" -> RoomIcon.Circle
    "Flamingo" -> RoomIcon.Rhombus
    "Jellyfish" -> RoomIcon.Triangle
    else -> RoomIcon.None
}

private fun MessageResponse.toMultiLangText() =
    if (ja != null && en != null) MultiLangText(jaTitle = ja, enTitle = en) else null
