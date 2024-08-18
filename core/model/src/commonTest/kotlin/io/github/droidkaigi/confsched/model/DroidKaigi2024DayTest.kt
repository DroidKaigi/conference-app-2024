package io.github.droidkaigi.confsched.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DroidKaigi2024DayOfOrNullTest {
    @Test
    fun `returns Workday on September 11 2024`() {
        val date = LocalDate(2024, 9, 11)

        runTest(
            expected = DroidKaigi2024Day.Workday,
            date.atStartOfDay(),
            date.at3pm(),
            date.atEndOfDay(),
        )
    }

    @Test
    fun `returns Day1 on September 12 2024`() {
        val date = LocalDate(2024, 9, 12)

        runTest(
            expected = DroidKaigi2024Day.ConferenceDay1,
            date.atStartOfDay(),
            date.at3pm(),
            date.atEndOfDay(),
        )
    }

    @Test
    fun `returns Day2 on September 13 2024`() {
        val date = LocalDate(2024, 9, 13)

        runTest(
            expected = DroidKaigi2024Day.ConferenceDay2,
            date.atStartOfDay(),
            date.at3pm(),
            date.atEndOfDay(),
        )
    }

    @Test
    fun `returns null outside the conference`() {
        runTest(
            expected = null,
            LocalDate(2024, 8, 1).at3pm(),
            LocalDate(2024, 9, 10).atEndOfDay(),
            LocalDate(2024, 9, 14).atStartOfDay(),
            LocalDate(2024, 12, 31).atEndOfDay(),
        )
    }

    private fun runTest(expected: DroidKaigi2024Day?, vararg instants: Instant) {
        instants.forEachIndexed { index, instant ->
            val actual = DroidKaigi2024Day.ofOrNull(instant)

            assertTimestamp(instant, expected, actual)
        }
    }
}

class DroidKaigi2024DayInitialSelectedTabDayTest {
    @Test
    fun `initialSelectedTabDay returns Day1 on any date except for the second conference day`() {
        runTest(
            expected = DroidKaigi2024Day.ConferenceDay1,
            // Before the conference
            LocalDate(2024, 8, 1).atStartOfDay(),
            // Workday
            LocalDate(2024, 9, 11).atStartOfDay(),
            LocalDate(2024, 9, 11).atEndOfDay(),
            // Day1
            LocalDate(2024, 9, 12).atStartOfDay(),
            LocalDate(2024, 9, 12).at3pm(),
            LocalDate(2024, 9, 12).atEndOfDay(),
            // After the conference
            LocalDate(2024, 9, 14).atStartOfDay(),
            LocalDate(2024, 9, 14).at3pm(),
            LocalDate(2024, 9, 14).atEndOfDay(),
            LocalDate(2024, 12, 31).atStartOfDay(),
        )
    }

    @Test
    fun `initialSelectedTabDay returns Day2 on the second conference day`() {
        runTest(
            expected = DroidKaigi2024Day.ConferenceDay2,
            LocalDate(2024, 9, 13).atStartOfDay(),
            LocalDate(2024, 9, 13).at3pm(),
            LocalDate(2024, 9, 13).atEndOfDay(),
        )
    }

    private fun runTest(expected: DroidKaigi2024Day, vararg instants: Instant) {
        instants.forEachIndexed { index, instant ->
            val clock = TestClock(instant)
            val actual = DroidKaigi2024Day.initialSelectedTabDay(clock)

            assertTimestamp(instant, expected, actual)
        }
    }

    private class TestClock(private val instant: Instant) : Clock {
        override fun now() = instant
    }
}

private val tz = TimeZone.of("UTC+9")

private fun LocalDate.atStartOfDay() = this.atStartOfDayIn(tz)
private fun LocalDate.at3pm() = this.atTime(15, 0).toInstant(tz)
private fun LocalDate.atEndOfDay() = this.atTime(23, 59, 59, 999_999_999).toInstant(tz)

private fun assertTimestamp(instant: Instant, expected: Any?, actual: Any?) {
    assertEquals(
        expected = expected,
        actual = actual,
        message = "Expected $expected, but was $actual for timestamp: ${instant.toLocalDateTime(tz)}",
    )
}
