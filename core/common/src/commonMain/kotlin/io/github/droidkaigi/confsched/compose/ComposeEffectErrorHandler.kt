package io.github.droidkaigi.confsched.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.State
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.takahirom.rin.produceRetainedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

interface ComposeEffectErrorHandler {
    suspend fun emit(throwable: Throwable)
}

val LocalComposeEffectErrorHandler = staticCompositionLocalOf<ComposeEffectErrorHandler> {
    object : ComposeEffectErrorHandler {
        override suspend fun emit(throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}

@Composable
fun SafeLaunchedEffect(key: Any?, block: suspend CoroutineScope.() -> Unit) {
    val composeEffectErrorHandler = LocalComposeEffectErrorHandler.current
    LaunchedEffect(key) {
        try {
            block()
        } catch (e: Exception) {
            ensureActive()
            composeEffectErrorHandler.emit(e)
        }
    }
}

@Composable
fun <T : R, R> Flow<T>.safeCollectAsState(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext,
): State<R> {
    val composeEffectErrorHandler = LocalComposeEffectErrorHandler.current
    // See collectAsState
    return produceRetainedState(initial, this, context) {
        try {
            if (context == EmptyCoroutineContext) {
                collect { value = it as R }
            } else {
                withContext(context) {
                    collect { value = it as R }
                }
            }
        } catch (e: Throwable) {
            composeEffectErrorHandler.emit(e)
        }
    }
}

@Composable
fun <T : R, R> StateFlow<T>.safeCollectAsState(
    context: CoroutineContext = EmptyCoroutineContext,
): State<R> {
    return safeCollectAsState(value, context)
}
