package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface

@Composable
actual fun dotGothic16FontFamily(): FontFamily {
    val skTypeface = loadCustomFont("dot_gothic16_regular")

    return try {
        FontFamily(Typeface(skTypeface))
    } finally {
        skTypeface.close()
    }
}

private fun loadCustomFont(name: String): Typeface {
    val fontMgr = FontMgr.default

    return fontMgr.matchFamilyStyle(name, FontStyle.NORMAL)
        ?: fontMgr.matchFamilyStyle(null, FontStyle.NORMAL) // default system font.
        ?: Typeface.makeEmpty()
}
