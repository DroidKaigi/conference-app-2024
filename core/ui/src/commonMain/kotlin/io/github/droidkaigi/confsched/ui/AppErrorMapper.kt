package io.github.droidkaigi.confsched.ui

import io.ktor.util.reflect.instanceOf

internal fun Throwable.toApplicationErrorMessage(
    composeResourceErrorMessages: List<ComposeResourceErrorMessage>? = null,
): String {
    composeResourceErrorMessages?.forEach {
        if (this.instanceOf(it.appError)) return it.message
    }
    return message ?: ""
}
