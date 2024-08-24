package io.github.droidkaigi.confsched.droidkaigiui

import java.util.UUID

actual fun randomUUIDHash(): Int = UUID.randomUUID().hashCode()
