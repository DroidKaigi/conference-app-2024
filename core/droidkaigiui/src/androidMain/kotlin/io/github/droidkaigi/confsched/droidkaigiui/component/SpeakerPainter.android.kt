package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil3.request.ImageRequest
import coil3.request.placeholder
import io.github.droidkaigi.confsched.core.droidkaigiui.R
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter

@Composable
actual fun speakerPainter(url: String): Painter {
    return rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .placeholder(R.drawable.icon_place_hoolder)
            .build(),
    )
}
