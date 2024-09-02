package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil3.request.ImageRequest
import coil3.request.placeholder
import io.github.droidkaigi.confsched.core.droidkaigiui.R
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter

@Composable
actual fun speakerPainter(url: String): Painter {
    val context = LocalContext.current

    val imageRequest = remember(url, context) {
        // TODO Use DrawableResource
        // https://github.com/DroidKaigi/conference-app-2024/pull/864/files#r1736437080
        // https://github.com/coil-kt/coil/issues/2077
        ImageRequest.Builder(context)
            .data(url)
            .placeholder(R.drawable.icon_place_hoolder)
            .build()
    }

    return rememberAsyncImagePainter(model = imageRequest)
}
