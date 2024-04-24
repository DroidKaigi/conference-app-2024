package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import io.github.takahirom.rin.collectAsRetainedState

@Composable
fun <T : Any> rememberCreationExtra(key: String, initialValue: T): T {
    // HACK: Retrieve NavBackStackEntry from LocalLifecycleOwner
    // Waiting for multiplatform savedState implementation
    val creationExtras: SavedStateHandle = (LocalLifecycleOwner.current as NavBackStackEntry).savedStateHandle
    val extra by creationExtras.getStateFlow(key, initialValue).collectAsRetainedState()
    return extra
}
