package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter

@Composable
actual fun speakerPainter(url: String): Painter {
    return rememberAsyncImagePainter(url)
}
