package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.currentComposer
import app.cash.molecule.RecompositionMode.ContextClock
import app.cash.molecule.launchMolecule
import io.github.droidkaigi.confsched.compose.ComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.LocalComposeEffectErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

interface ComposeViewModel<Event, UiState> {
    val uiState: StateFlow<UiState>
    fun take(event: Event)
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
    composeCoroutineContext: CoroutineContext,
    val userMessageStateHolder: UserMessageStateHolder,
    val content: @Composable ComposeViewModel<Event, Model>.(Flow<Event>) -> Model,
) :
    ComposeViewModel<Event, Model> {
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
                val sharedFlow = MutableSharedFlow<Throwable>(extraBufferCapacity = 20)
                override val errors: Flow<Throwable> = sharedFlow

                override suspend fun emit(throwable: Throwable) {
                    throwable.printStackTrace()
                    sharedFlow.emit(throwable)
                }
            }
            return@launchMolecule CompositionLocalProviderWithReturnValue<Model>(
                LocalComposeEffectErrorHandler provides errorHandler
            ) {
                content(events)
            }
        }
    }

    @Composable
    @OptIn(InternalComposeApi::class)
    fun <T> CompositionLocalProviderWithReturnValue(
        value: ProvidedValue<*>,
        content: @Composable () -> T,
    ): T {
        currentComposer.startProvider(value)
        val result = content()
        currentComposer.endProvider()
        return result
    }

    override fun take(event: Event) {
        if (!events.tryEmit(event)) {
            error("Event buffer overflow.")
        }
    }
}
