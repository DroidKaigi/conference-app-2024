package io.github.droidkaigi.confsched.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableSharedFlow

typealias EventEmitter<T> = MutableSharedFlow<T>

@Composable
fun <T> rememberEventEmitter(): EventEmitter<T> {
    return remember {
        MutableSharedFlow(extraBufferCapacity = 20)
    }
}
