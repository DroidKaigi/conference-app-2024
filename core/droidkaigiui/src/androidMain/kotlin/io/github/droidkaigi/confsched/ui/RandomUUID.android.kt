package io.github.droidkaigi.confsched.ui

import java.util.UUID

actual fun randomUUIDHash(): Int = UUID.randomUUID().hashCode()
