package io.github.droidkaigi.confsched.shared

import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual fun runOnMainThread(block: () -> Unit) {
    dispatch_async(dispatch_get_main_queue(), block)
}
