package io.github.droidkaigi.confsched.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

actual fun generateColoredImageBase64(color: Color, size: IntSize): String {
    // Since this code is never used on the iOS side, it returns a fixed number of empty characters
    return ""
}
