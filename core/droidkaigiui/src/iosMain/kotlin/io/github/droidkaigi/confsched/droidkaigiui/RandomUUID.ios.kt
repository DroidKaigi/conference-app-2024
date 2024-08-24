package io.github.droidkaigi.confsched.droidkaigiui

import platform.Foundation.NSUUID

actual fun randomUUIDHash(): Int = NSUUID().hash.toInt()
