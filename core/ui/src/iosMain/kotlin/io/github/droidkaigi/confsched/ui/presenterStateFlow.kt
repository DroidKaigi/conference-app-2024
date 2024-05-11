package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import io.github.droidkaigi.confsched.compose.CompositionLocalProviderWithReturnValue
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlin.reflect.KClass

@Suppress("unused")
fun <EVENT, UISTATE> presenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: Flow<EVENT>,
    presenter: @Composable (events: Flow<EVENT>) -> UISTATE,
): Flow<UISTATE> {
    return moleculeFlow(RecompositionMode.Immediate) {
        val lifecycleRegistry = remember {
            object : LifecycleOwner {
                override val lifecycle: Lifecycle = LifecycleRegistry(this)
            }
        }
        var uiState by remember { mutableStateOf<UISTATE?>(null) }
        CompositionLocalProvider(
            LocalRepositories provides repositories,
            LocalLifecycleOwner provides lifecycleRegistry
        ) {
            NavHost(rememberNavController(), startDestination = "root") {
                composable("root") {
                    val newUiState = presenter(events)
                    LaunchedEffect(newUiState) {
                        uiState = newUiState
                    }
                }
            }
        }
        uiState
    }
        .filterNotNull()
}


