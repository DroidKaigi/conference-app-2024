package io.github.droidkaigi.confsched.model

import kotlinx.datetime.LocalTime

class EventMapEvent(
    val name: String,
    val roomName: String,
    val dateLabel: String,
    val isFavorite: Boolean,
    val description: String,
    private val startTime: LocalTime,
    private val endTime: LocalTime,
) {
    val timeDuration: String
        get() = "$startTime ~ $endTime"
}

fun createSampleEventMapEvent(
    isFavorite: Boolean = false,
) = EventMapEvent(
    name = "ランチミートアップ",
    roomName = "Flamingo",
    dateLabel = "DAY1",
    isFavorite = isFavorite,
    description = "様々なテーマごとに集まって、一緒にランチを食べながらお話ししましょう。席に限りがありますので、お弁当受け取り後お早めにお越しください。",
    startTime = LocalTime(10, 30),
    endTime = LocalTime(12, 30),
)
