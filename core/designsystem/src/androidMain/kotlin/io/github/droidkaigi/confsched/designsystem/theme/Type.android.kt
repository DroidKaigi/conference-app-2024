package io.github.droidkaigi.confsched.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import conference_app_2024.core.designsystem.generated.resources.dot_gothic16_regular
import conference_app_2024.core.designsystem.generated.resources.noto_sans_jp_regular
import io.github.droidkaigi.confsched.designsystem.DesignSystemRes
import org.jetbrains.compose.resources.Font

@Composable
actual fun dotGothic16FontFamily(): FontFamily = FontFamily(
    Font(DesignSystemRes.font.dot_gothic16_regular),
)

@Composable
actual fun notoSansFontFamily(): FontFamily = FontFamily(
    Font(DesignSystemRes.font.noto_sans_jp_regular)
)
