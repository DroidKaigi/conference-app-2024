package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun provideAboutHeaderTitlePainter(enableAnimation: Boolean = true): Painter
