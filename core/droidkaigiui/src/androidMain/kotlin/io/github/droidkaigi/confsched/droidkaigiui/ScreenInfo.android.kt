package io.github.droidkaigi.confsched.droidkaigiui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun getScreenSizeInfo(): ScreenInfo {
//    val density = LocalDensity.current
//    val config = LocalConfiguration.current
//    val hDp = config.screenHeightDp.dp
//    val wDp = config.screenWidthDp.dp
    return ScreenInfo(
        isPort = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT,
    )
}
