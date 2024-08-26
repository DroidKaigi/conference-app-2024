package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun rememberAsyncImagePainter(url: String): Painter {
    return coil3.compose.rememberAsyncImagePainter(
        model = url,
    )
}
