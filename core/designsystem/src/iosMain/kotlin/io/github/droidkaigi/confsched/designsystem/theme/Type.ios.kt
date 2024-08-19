package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface

@Composable
actual fun dotGothic16FontFamily(): FontFamily {
    return FontFamily(Typeface(loadCustomFont("dot_gothic16_regular")))
}

private fun loadCustomFont(name: String): Typeface {
    return Typeface.makeFromName(name, FontStyle.NORMAL)
}
