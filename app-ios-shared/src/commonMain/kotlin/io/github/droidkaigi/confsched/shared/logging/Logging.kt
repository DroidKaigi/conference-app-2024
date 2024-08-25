package io.github.droidkaigi.confsched.shared.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger

internal object Logging {
    fun initialize() {
        with(Logger) {
            setTag("KaigiApp")
            setLogWriters(getPlatformLogWriters())
        }
    }
}

internal expect fun getPlatformLogWriters(): List<LogWriter>
