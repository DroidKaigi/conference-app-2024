package io.github.droidkaigi.confsched.ui

actual fun canShowLargeVector(): Boolean {
    // The process is only relevant to Android, so it always returns true.
    return true
}
