package io.github.droidkaigi.confsched.model

import androidx.compose.ui.unit.IntSize

internal expect fun generateWhiteImageBase64(size: IntSize = IntSize(400, 400)): String
