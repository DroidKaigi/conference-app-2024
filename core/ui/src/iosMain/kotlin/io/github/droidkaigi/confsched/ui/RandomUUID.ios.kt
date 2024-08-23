package io.github.droidkaigi.confsched.ui

import platform.Foundation.NSUUID

actual fun randomUUIDHash(): Int = NSUUID().hash.toInt()
