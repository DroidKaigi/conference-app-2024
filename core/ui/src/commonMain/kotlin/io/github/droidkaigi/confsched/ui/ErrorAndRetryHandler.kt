package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.compose.ComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.CompositionLocalProviderWithReturnValue
import io.github.droidkaigi.confsched.compose.LocalComposeEffectErrorHandler
import io.github.droidkaigi.confsched.designsystem.strings.Strings
import io.github.droidkaigi.confsched.ui.UserMessageResult.ActionPerformed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry

fun <T> Flow<T>.handleErrorAndRetry(
    actionLabel: Strings<*>,
    userMessageStateHolder: UserMessageStateHolder,
) = retry { throwable ->
    // TODO: Introduce logger
    throwable.printStackTrace()
    val messageResult = userMessageStateHolder.showMessage(
        message = throwable.toApplicationErrorMessage(),
        actionLabel = actionLabel.asString(),
    )

    val retryPerformed = messageResult == ActionPerformed

    retryPerformed
}.catch { /* Do nothing if the user dose not retry. */ }

@Composable
fun <T> applicationErrorHandler(
    userMessageStateHolder: UserMessageStateHolder,
    block: @Composable () -> T,
): T {
    val handler = remember(userMessageStateHolder) {
        object : ComposeEffectErrorHandler {
            override suspend fun emit(throwable: Throwable) {
                val a = throwable.toApplicationErrorMessage()
                userMessageStateHolder.showMessage(
                    message = a,
                    actionLabel = null,
                )
            }
        }
    }
    return CompositionLocalProviderWithReturnValue(LocalComposeEffectErrorHandler provides handler) {
        block()
    }
}
