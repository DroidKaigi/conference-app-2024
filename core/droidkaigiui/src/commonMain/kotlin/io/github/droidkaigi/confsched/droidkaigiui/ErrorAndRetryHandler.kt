package io.github.droidkaigi.confsched.droidkaigiui

import io.github.droidkaigi.confsched.droidkaigiui.UserMessageResult.ActionPerformed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry

fun <T> Flow<T>.handleErrorAndRetry(
    actionLabel: String,
    userMessageStateHolder: UserMessageStateHolder,
) = retry { throwable ->
    // TODO: Introduce logger
    throwable.printStackTrace()
    val messageResult = userMessageStateHolder.showMessage(
        message = throwable.toApplicationErrorMessage(),
        actionLabel = actionLabel,
    )

    val retryPerformed = messageResult == ActionPerformed

    retryPerformed
}.catch { /* Do nothing if the user dose not retry. */ }
