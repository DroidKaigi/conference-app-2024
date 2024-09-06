package io.github.droidkaigi.confsched.shared.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.XcodeSeverityWriter

internal object Logging {
    fun initialize() {
        with(Logger) {
            setTag("KaigiApp")
            setLogWriters(XcodeSeverityWriter())
        }
    }
}
