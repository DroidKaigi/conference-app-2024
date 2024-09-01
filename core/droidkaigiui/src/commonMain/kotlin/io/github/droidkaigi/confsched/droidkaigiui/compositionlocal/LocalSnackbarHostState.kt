package io.github.droidkaigi.confsched.droidkaigiui.compositionlocal

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

@Suppress("CompositionLocalAllowlist")
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState?> {
    null
}
