package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProvideAndroidContextToComposeResource() {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        PreviewContextConfigurationEffect()
    }
}
