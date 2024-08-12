package io.github.droidkaigi.confsched.data.eventmap

import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.model.fake
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import okio.IOException

public class FakeEventMapApiClient : EventMapApiClient {

    public sealed class Status : EventMapApiClient {
        public data object Operational : Status() {
            override suspend fun eventMapEvents(): PersistentList<EventMapEvent> {
                return List(10) {
                    EventMapEvent.fake()
                }.toPersistentList()
            }
        }

        public data object Error : Status() {
            override suspend fun eventMapEvents(): PersistentList<EventMapEvent> {
                throw IOException("Fake IO Exception")
            }
        }
    }

    private var status: Status = Status.Operational

    public fun setup(status: Status) {
        this.status = status
    }

    override suspend fun eventMapEvents(): PersistentList<EventMapEvent> {
        return status.eventMapEvents()
    }
}
