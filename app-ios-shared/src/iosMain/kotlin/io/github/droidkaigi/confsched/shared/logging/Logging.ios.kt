package io.github.droidkaigi.confsched.shared.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.XcodeSeverityWriter

internal actual fun getPlatformLogWriters(): List<LogWriter> = listOf(XcodeSeverityWriter())
