package io.github.droidkaigi.confsched.model

data class EventMapEvent(
    val name: MultiLangText,
    val roomName: MultiLangText,
    val description: MultiLangText,
)

fun createSampleEventMapEvent() = EventMapEvent(
    name = MultiLangText("ランチミートアップ", "Lunch Meetup"),
    roomName = MultiLangText("Iguana", "Iguana"),
    description = MultiLangText(
        "様々なテーマごとに集まって、一緒にランチを食べながらお話ししましょう。席に限りがありますので、お弁当受け取り後お早めにお越しください。",
        "Let's gather for lunch and chat about various topics. Seats are limited, so please come soon after receiving your lunch box.",
    ),
)
