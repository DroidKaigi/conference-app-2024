package io.github.droidkaigi.confsched.testing.utils

expect inline fun <T> readResourceFile(filePath: okio.Path, block: okio.BufferedSource.() -> T): T
