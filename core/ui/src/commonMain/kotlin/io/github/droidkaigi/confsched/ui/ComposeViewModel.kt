package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.PausableMonotonicFrameClock
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.monotonicFrameClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import app.cash.molecule.RecompositionMode.ContextClock
import app.cash.molecule.launchMolecule
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.compose.ComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.CompositionLocalProviderWithReturnValue
import io.github.droidkaigi.confsched.compose.LocalComposeEffectErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

interface ComposeViewModel<Event, UiState> : UserMessageStateHolder {
    val uiState: StateFlow<UiState>
    fun take(event: Event)

    fun activeLifecycleWhile(lifecycle: Lifecycle)
}

class KmpViewModelLifecycle {
    fun interface OnClearedListener {
        fun onCleared()
    }

    private val onClearedListeners = mutableListOf<OnClearedListener>()
    fun addOnClearedListener(listener: OnClearedListener) {
        onClearedListeners.add(listener)
    }

    fun onCleared() {
        onClearedListeners.forEach { it.onCleared() }
    }
}

class DefaultComposeViewModel<Event, Model>(
    viewModelLifecycle: KmpViewModelLifecycle,
    val composeCoroutineContext: CoroutineContext,
    val content: @Composable ComposeViewModel<Event, Model>.(Flow<Event>) -> Model,
) :
    ComposeViewModel<Event, Model>,
    UserMessageStateHolder by UserMessageStateHolderImpl() {
    private val scope = CoroutineScope(composeCoroutineContext)

    init {
        viewModelLifecycle.addOnClearedListener {
            scope.cancel()
        }
    }

    // Events have a capacity large enough to handle simultaneous UI events, but
    // small enough to surface issues if they get backed up for some reason.
    private val events = MutableSharedFlow<Event>(extraBufferCapacity = 20)

    override val uiState: StateFlow<Model> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(mode = ContextClock) {
            val errorHandler = object : ComposeEffectErrorHandler {
                override suspend fun emit(throwable: Throwable) {
                }
            }
            return@launchMolecule CompositionLocalProviderWithReturnValue<Model>(
                LocalComposeEffectErrorHandler provides errorHandler,
            ) {
                content(events)
            }
        }
    }
    @OptIn(ExperimentalComposeApi::class) private val lifecycleObserver =
        object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                (composeCoroutineContext.monotonicFrameClock as PausableMonotonicFrameClock).resume()
                // can be called multiple times
                Logger.d { "$this:start $owner" }
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                (composeCoroutineContext.monotonicFrameClock as PausableMonotonicFrameClock).pause()
                Logger.d { "$this:stop $owner" }
            }
        }

    private var currentLifecycle: Lifecycle? = null
    override fun activeLifecycleWhile(lifecycle: Lifecycle) {
        currentLifecycle?.removeObserver(lifecycleObserver)
        lifecycle.addObserver(lifecycleObserver)
        currentLifecycle = lifecycle
    }

    override fun take(event: Event) {
        if (!events.tryEmit(event)) {
            error("Event buffer overflow.")
        }
    }
}
