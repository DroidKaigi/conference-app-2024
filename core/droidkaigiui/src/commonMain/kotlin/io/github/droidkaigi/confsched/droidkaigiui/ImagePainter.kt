package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil3.request.ImageRequest

@Composable
fun rememberAsyncImagePainter(url: String): Painter {
    return coil3.compose.rememberAsyncImagePainter(
        model = url,
    )
}

@Composable
fun rememberAsyncImagePainter(model: ImageRequest): Painter {
    return coil3.compose.rememberAsyncImagePainter(
        model = model,
    )
}
