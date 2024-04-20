package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable

data class ScreenInfo(val isPort: Boolean)

@Composable
expect fun getScreenSizeInfo(): ScreenInfo
