package io.github.droidkaigi.confsched.shared

import kotlinx.coroutines.flow.MutableSharedFlow

class SkieKotlinSharedFlowFactory<T : Any> {
    fun createSkieKotlinSharedFlow(
        replay: Int = 0,
        extraBufferCapacity: Int = 0,
    ): MutableSharedFlow<T> = MutableSharedFlow(
        replay = replay,
        extraBufferCapacity = extraBufferCapacity
    )
}
