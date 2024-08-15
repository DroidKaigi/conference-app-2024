package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import conference_app_2024.core.designsystem.generated.resources.dot_gothic16_regular
import io.github.droidkaigi.confsched.designsystem.DesignSystemRes
import org.jetbrains.compose.resources.Font
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface

@Composable
actual fun dotGothic16FontFamily(): FontFamily = FontFamily(
    Font(DesignSystemRes.font.dot_gothic16_regular),
)
