package io.github.droidkaigi.confsched.testing.utils

import okio.FileSystem
import okio.Path.Companion.toPath

actual inline fun <T> readResourceFile(filePath: okio.Path, block: okio.BufferedSource.() -> T): T {
    return FileSystem.SYSTEM.read(
        "src".toPath().div("commonTest").div("resources").div(filePath),
    ) {
        block()
    }
}
