package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import conference_app_2024.core.ui.generated.resources.about_header_title
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun provideAboutHeaderTitlePainter(): Painter {
    return painterResource(UiRes.drawable.about_header_title)
}
