package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.bundle.Bundle
import androidx.navigation.NavBackStackEntry

@Composable
fun <T : Any> rememberNavigationArgument(key: String, initialValue: T): T {
    // HACK: Retrieve NavBackStackEntry from LocalLifecycleOwner
    // Waiting for multiplatform savedState implementation
    val navBackStackEntry = LocalLifecycleOwner.current as NavBackStackEntry
    val bundle: Bundle? = navBackStackEntry.arguments
    return remember { bundle?.get(key) as? T? ?: initialValue }
}
