package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue

@Composable
inline fun KaigiAppCompositionLocalProvider(
    vararg locals: ProvidedValue<*>,
    crossinline content: @Composable () -> Unit,
) {
    CompositionLocalProvider(*locals) {
        if (isTest()) {
            ProvideAndroidContextToComposeResource()
        }
        content()
    }
}
