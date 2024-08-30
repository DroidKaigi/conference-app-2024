package io.github.droidkaigi.confsched.droidkaigiui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.handleErrorAndRetryAction(
    actionLabel: String,
    userMessageStateHolder: UserMessageStateHolder,
    retryAction: suspend ((Throwable) -> Unit),
) = catch { throwable ->
    val messageResult = userMessageStateHolder.showMessage(
        message = throwable.toApplicationErrorMessage(),
        actionLabel = actionLabel,
    )

    if (messageResult == UserMessageResult.ActionPerformed) {
        retryAction(throwable)
    }
}.catch { /* Do nothing if the user dose not retry. */ }
