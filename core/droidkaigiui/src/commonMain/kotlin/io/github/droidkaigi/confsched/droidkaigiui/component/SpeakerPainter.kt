package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun speakerPainter(url: String): Painter
