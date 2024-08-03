package io.github.droidkaigi.confsched.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

public enum class DroidKaigi2024Day(
    public val dayIndex: Int,
    public val dayOfMonth: Int,
    public val start: Instant,
    public val end: Instant,
) {
    Workday(
        dayIndex = 0,
        dayOfMonth = 11,
        start = LocalDateTime
            .parse("2024-09-11T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2024-09-12T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
    ),
    ConferenceDay1(
        dayIndex = 1,
        dayOfMonth = 12,
        start = LocalDateTime
            .parse("2024-09-12T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2024-09-13T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
    ),
    ConferenceDay2(
        dayIndex = 2,
        dayOfMonth = 13,
        start = LocalDateTime
            .parse("2024-09-13T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2024-09-14T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
    ),
    ;

    fun getDropDownText(language: String): String {
        val japanese = "ja"

        val date = this.start.toLocalDateTime(TimeZone.currentSystemDefault())

        val year = if (language == japanese) {
            "${date.year}年"
        } else {
            "${date.year}"
        }

        val month = if (language == japanese) {
            "${date.monthNumber}月"
        } else {
            date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        }

        val day = if (language == japanese) {
            "${date.dayOfMonth}日"
        } else {
            "${date.dayOfMonth}th"
        }

        return "${this.name} ($year $month $day)"
    }

    public companion object {
        public fun ofOrNull(time: Instant): DroidKaigi2024Day? {
            return entries.firstOrNull {
                time in it.start..it.end
            }
        }

        /**
         * @return appropriate initial day for now
         */
        fun initialSelectedDay(clock: Clock): DroidKaigi2024Day {
            if (clock.now() < Workday.start.minus(1.days)) return ConferenceDay1
            val reversedEntries = entries.sortedByDescending { it.dayIndex }
            var selectedDay = reversedEntries.last()
            for (entry in reversedEntries) {
                if (clock.now() <= entry.end) selectedDay = entry
            }
            return selectedDay
        }
    }
}
