package io.github.droidkaigi.confsched.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

typealias EventFlow<T> = MutableSharedFlow<T>

@Composable
fun <T> rememberEventFlow(): EventFlow<T> {
    return remember {
        MutableSharedFlow(extraBufferCapacity = 20)
    }
}

@Composable
fun <EVENT> EventEffect(
    eventFlow: EventFlow<EVENT>,
    block: suspend CoroutineScope.(EVENT) -> Unit,
) {
    SafeLaunchedEffect(eventFlow) {
        supervisorScope {
            eventFlow.collect { event ->
                launch {
                    block(event)
                }
            }
        }
    }
}
