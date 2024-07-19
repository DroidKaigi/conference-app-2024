package io.github.droidkaigi.confsched.sessions.component

import io.github.droidkaigi.confsched.model.RoomType.RoomA
import io.github.droidkaigi.confsched.model.RoomType.RoomB
import io.github.droidkaigi.confsched.model.RoomType.RoomC
import io.github.droidkaigi.confsched.model.RoomType.RoomD
import io.github.droidkaigi.confsched.model.RoomType.RoomDE
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.fake
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class PreviewTimeTableItemRoomProvider : PreviewParameterProvider<TimetableItem> {
    override val values: Sequence<TimetableItem>
        get() = sequenceOf(
            Session.fake(),
            Session.fake().copy(room = Session.fake().room.copy(type = RoomC)),
            Session.fake().copy(room = Session.fake().room.copy(type = RoomA)),
            Session.fake().copy(room = Session.fake().room.copy(type = RoomB)),
            Session.fake().copy(room = Session.fake().room.copy(type = RoomD)),
            Session.fake().copy(room = Session.fake().room.copy(type = RoomDE)),
            Session.fake().copy(speakers = persistentListOf(Session.fake().speakers.first())),
            Session.fake().copy(speakers = persistentListOf()),
        )
}
