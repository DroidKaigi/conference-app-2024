package io.github.droidkaigi.confsched.droidkaigiui.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Suppress("CompositionLocalAllowlist")
val LocalClock = staticCompositionLocalOf<Clock> {
    Clock.System
}

val FakeClock: Clock = FakeClockImpl(Instant.parse("2023-09-14T10:00:00.000Z"))

@Suppress("FunctionName")
fun FakeClock(instant: Instant): Clock = FakeClockImpl(instant)

private class FakeClockImpl(private val instant: Instant) : Clock {
    override fun now(): Instant = instant
}
