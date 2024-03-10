package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.rememberImagePainter

@Composable
fun rememberAsyncImagePainter(url: String): Painter {
    return rememberImagePainter(url = url)
}
