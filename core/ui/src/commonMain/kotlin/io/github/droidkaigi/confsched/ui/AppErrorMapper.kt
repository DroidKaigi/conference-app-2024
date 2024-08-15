package io.github.droidkaigi.confsched.ui

import io.github.droidkaigi.confsched.model.AppError.InternetConnectionException
import io.github.droidkaigi.confsched.ui.ComposeResourceErrorMessageType.ConnectionFailed

internal fun Throwable.toApplicationErrorMessage(
    composeResourceErrorMessage: List<ComposeResourceErrorMessage>? = null,
): String {
    composeResourceErrorMessage?.forEach {
        if (it.type == ConnectionFailed && this is InternetConnectionException) {
            return it.message
        }
    }
    return message ?: ""
}
