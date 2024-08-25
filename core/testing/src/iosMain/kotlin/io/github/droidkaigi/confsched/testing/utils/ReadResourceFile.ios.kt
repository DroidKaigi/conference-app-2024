package io.github.droidkaigi.confsched.testing.utils

import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSBundle

actual inline fun <T> readResourceFile(filePath: Path, block: BufferedSource.() -> T): T {
    return FileSystem.SYSTEM.read(
        NSBundle.mainBundle.bundlePath.toPath().parent!!.parent!!.parent!!.parent!!
            .div("src").div("commonTest").div("resources").div(filePath)
    ) {
        block()
    }
}
