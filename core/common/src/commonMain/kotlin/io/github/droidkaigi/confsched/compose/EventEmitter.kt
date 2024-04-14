package io.github.droidkaigi.confsched.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableSharedFlow

typealias EventEmitter<T> = MutableSharedFlow<T>

@Composable
fun <T> rememberEventEmitter(): MutableSharedFlow<T> {
    return remember {
        MutableSharedFlow<T>(extraBufferCapacity = 20)
    }
}
