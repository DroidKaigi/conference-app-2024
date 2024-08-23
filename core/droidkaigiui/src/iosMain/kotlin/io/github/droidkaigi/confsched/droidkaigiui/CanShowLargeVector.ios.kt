package io.github.droidkaigi.confsched.droidkaigiui

actual fun canShowLargeVector(): Boolean {
    // The process is only relevant to Android, so it always returns true.
    return true
}
