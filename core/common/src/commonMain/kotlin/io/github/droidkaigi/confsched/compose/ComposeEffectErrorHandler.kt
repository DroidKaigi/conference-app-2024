package io.github.droidkaigi.confsched.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface ComposeEffectErrorHandler {
    val errors: Flow<Throwable>
    suspend fun emit(throwable: Throwable)
}

val LocalComposeEffectErrorHandler = staticCompositionLocalOf<ComposeEffectErrorHandler> {
    error("CompositionLocal Error not present")
}

@Composable
fun SafeLaunchedEffect(key: Any, block: suspend CoroutineScope.() -> Unit) {
    val composeEffectErrorHandler = LocalComposeEffectErrorHandler.current
    LaunchedEffect(key) {
        try {
            block()
        } catch (throwable: Throwable) {
            composeEffectErrorHandler.emit(throwable)
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
    return produceState(initial, this, context) {
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
