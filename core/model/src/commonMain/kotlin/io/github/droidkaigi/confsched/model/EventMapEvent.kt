package io.github.droidkaigi.confsched.model

import io.github.droidkaigi.confsched.model.RoomType.RoomF
import io.github.droidkaigi.confsched.model.RoomType.RoomG
import io.github.droidkaigi.confsched.model.RoomType.RoomH
import io.github.droidkaigi.confsched.model.RoomType.RoomI
import io.github.droidkaigi.confsched.model.RoomType.RoomIJ
import io.github.droidkaigi.confsched.model.RoomType.RoomJ
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

public data class EventMapEvent(
    val name: MultiLangText,
    val roomName: MultiLangText,
    val roomIcon: RoomIcon,
    val description: MultiLangText,
    val moreDetailsUrl: String?,
    val message: MultiLangText?,
) {
    public companion object
}

public fun EventMapEvent.Companion.fakes(): PersistentList<EventMapEvent> = RoomType.entries.map {
    EventMapEvent(
        name = MultiLangText("ランチミートアップ", "Lunch Meetup"),
        roomName = MultiLangText(it.toRoomName().jaTitle, it.toRoomName().enTitle),
        roomIcon = it.toRoomIcon(),
        description = MultiLangText(
            "様々なテーマごとに集まって、一緒にランチを食べながらお話ししましょう。席に限りがありますので、お弁当受け取り後お早めにお越しください。",
            "Let's gather for lunch and chat about various topics. Seats are limited, so please come soon after receiving your lunch box.",
        ),
        moreDetailsUrl = if (it.ordinal % 2 == 0) {
            "https://2024.droidkaigi.jp/"
        } else {
            null
        },
        message = if (it.ordinal % 3 == 0) {
            MultiLangText(
                "※こちらのイベントは時間が変更されました。",
                "※This event has been rescheduled.",
            )
        } else {
            null
        },
    )
}.toPersistentList()

private fun RoomType.toRoomName(): MultiLangText = when (this) {
    RoomF -> MultiLangText("Flamingo", "Flamingo")
    RoomG -> MultiLangText("Giraffe", "Giraffe")
    RoomH -> MultiLangText("Hedgehog", "Hedgehog")
    RoomI -> MultiLangText("Iguana", "Iguana")
    RoomJ -> MultiLangText("Jellyfish", "Jellyfish")
    RoomIJ -> MultiLangText("Iguana and Jellyfish", "Iguana and Jellyfish")
}

private fun RoomType.toRoomIcon(): RoomIcon = when (this) {
    RoomF -> RoomIcon.Rhombus
    RoomG -> RoomIcon.Circle
    RoomH -> RoomIcon.Diamond
    RoomI -> RoomIcon.Square
    RoomJ -> RoomIcon.Triangle
    RoomIJ -> RoomIcon.Square
}
