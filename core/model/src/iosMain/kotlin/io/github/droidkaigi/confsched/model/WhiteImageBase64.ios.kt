package io.github.droidkaigi.confsched.model

import androidx.compose.ui.unit.IntSize

internal actual fun generateWhiteImageBase64(size: IntSize): String {
    // Since this code is never used on the iOS side, it returns a fixed number of empty characters
    return ""
}
