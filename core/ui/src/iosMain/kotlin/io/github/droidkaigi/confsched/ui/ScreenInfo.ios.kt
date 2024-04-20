package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenSizeInfo(): ScreenInfo {
    val density = LocalDensity.current
    val config = LocalWindowInfo.current.containerSize
    return ScreenInfo(
        isPort = config.height > config.width
    )
}
