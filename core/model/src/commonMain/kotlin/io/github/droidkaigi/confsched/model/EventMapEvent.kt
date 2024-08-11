package io.github.droidkaigi.confsched.model

data class EventMapEvent(
    val name: String,
    val roomName: String,
    val dateLabel: String,
    val description: String,
)

fun createSampleEventMapEvent() = EventMapEvent(
    name = "ランチミートアップ",
    roomName = "Iguana",
    dateLabel = "DAY1",
    description = "様々なテーマごとに集まって、一緒にランチを食べながらお話ししましょう。席に限りがありますので、お弁当受け取り後お早めにお越しください。",
)
