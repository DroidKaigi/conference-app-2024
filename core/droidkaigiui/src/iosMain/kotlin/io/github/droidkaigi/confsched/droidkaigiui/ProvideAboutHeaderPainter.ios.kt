package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import conference_app_2024.core.droidkaigiui.generated.resources.about_header_title
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun provideAboutHeaderTitlePainter(enableAnimation: Boolean): Painter {
    return painterResource(DroidKaigiUiRes.drawable.about_header_title)
}
