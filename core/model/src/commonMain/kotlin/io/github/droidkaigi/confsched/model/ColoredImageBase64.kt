package io.github.droidkaigi.confsched.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

expect fun generateColoredImageBase64(
    color: Color = Color.White,
    size: IntSize = IntSize(400, 400),
): String
