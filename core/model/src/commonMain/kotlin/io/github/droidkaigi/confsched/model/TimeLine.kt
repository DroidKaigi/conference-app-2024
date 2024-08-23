package io.github.droidkaigi.confsched.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class TimeLine(
    private val currentTime: Instant,
    private val currentDay: DroidKaigi2024Day,
) {
    fun durationFromScheduleStart(targetDay: DroidKaigi2024Day): Duration? {
        if (currentDay != targetDay) return null
        val currentTimeSecondOfDay = currentTime.toLocalDateTime(TimeZone.currentSystemDefault()).time.toSecondOfDay()
        val scheduleStartTimeSecondOfDay = LocalTime(hour = 10, minute = 0).toSecondOfDay()
        return ((currentTimeSecondOfDay - scheduleStartTimeSecondOfDay) / 60).minutes
    }

    companion object {
        fun now(clock: Clock): TimeLine? {
            val currentTime = clock.now()
            val currentDay = DroidKaigi2024Day.ofOrNull(currentTime)
            return currentDay?.let {
                TimeLine(
                    currentTime = currentTime,
                    currentDay = currentDay,
                )
            }
        }
    }
}
