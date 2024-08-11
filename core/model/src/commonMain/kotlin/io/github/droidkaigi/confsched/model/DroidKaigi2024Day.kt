package io.github.droidkaigi.confsched.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

public enum class DroidKaigi2024Day(
    public val dayIndex: Int,
    public val visibleForUsers: Boolean,
    public val dayOfMonth: Int,
    public val start: Instant,
    public val end: Instant,
) {
    // We are not using Workday sessions in the app
    Workday(
        dayIndex = 0,
        visibleForUsers = false,
        dayOfMonth = 11,
        start = LocalDateTime
            .parse("2024-09-11T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2024-09-11T23:59:59")
            .toInstant(TimeZone.of("UTC+9")),
    ),
    ConferenceDay1(
        dayIndex = 1,
        visibleForUsers = true,
        dayOfMonth = 12,
        start = LocalDateTime
            .parse("2024-09-12T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2024-09-12T23:59:59")
            .toInstant(TimeZone.of("UTC+9")),
    ),
    ConferenceDay2(
        dayIndex = 2,
        visibleForUsers = true,
        dayOfMonth = 13,
        start = LocalDateTime
            .parse("2024-09-13T00:00:00")
            .toInstant(TimeZone.of("UTC+9")),
        end = LocalDateTime
            .parse("2024-09-13T23:59:59")
            .toInstant(TimeZone.of("UTC+9")),
    ),
    ;

    fun tabIndex(): Int {
        return entries
            .sortedBy { it.dayIndex }
            .filter { it.visibleForUsers }
            .indexOf(this)
    }

    fun monthAndDay(): String {
        return "9/$dayOfMonth"
    }

    public companion object {
        public fun ofOrNull(time: Instant): DroidKaigi2024Day? {
            return entries.firstOrNull {
                time in it.start..it.end
            }
        }

        fun visibleDays(): List<DroidKaigi2024Day> {
            return entries.filter { it.visibleForUsers }
        }

        /**
         * @return appropriate initial day for now
         */
        fun initialSelectedTabDay(clock: Clock): DroidKaigi2024Day {
            val reversedEntries = visibleDays().sortedByDescending { it.dayIndex }
            var selectedDay = reversedEntries.last()
            for (entry in reversedEntries) {
                if (clock.now() <= entry.end) selectedDay = entry
            }
            return selectedDay
        }
    }
}
