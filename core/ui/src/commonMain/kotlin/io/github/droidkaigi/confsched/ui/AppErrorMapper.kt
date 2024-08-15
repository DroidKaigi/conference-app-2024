package io.github.droidkaigi.confsched.ui

import io.github.droidkaigi.confsched.model.AppError.InternetConnectionException
import io.github.droidkaigi.confsched.ui.ComposeResourceErrorMessageType.ConnectionFailed

internal fun Throwable.toApplicationErrorMessage(
    composeResourceErrorMessages: List<ComposeResourceErrorMessage>? = null,
): String {
    composeResourceErrorMessages?.forEach {
        if (it.type == ConnectionFailed && this is InternetConnectionException) {
            return it.message
        }
    }
    return message ?: ""
}
