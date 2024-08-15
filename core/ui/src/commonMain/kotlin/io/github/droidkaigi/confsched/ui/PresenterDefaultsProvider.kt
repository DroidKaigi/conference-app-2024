package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.compose.ComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.LocalComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.compositionLocalProviderWithReturnValue

@Composable
fun <T> providePresenterDefaults(
    userMessageStateHolder: UserMessageStateHolder = rememberUserMessageStateHolder(),
    block: @Composable (UserMessageStateHolder) -> T,
): T {
    val composeResourceErrorMessage = ComposeResourceErrorMessages()
    val handler = remember(userMessageStateHolder) {
        object : ComposeEffectErrorHandler {
            override suspend fun emit(throwable: Throwable) {
                val message = throwable.toApplicationErrorMessage(composeResourceErrorMessage)
                userMessageStateHolder.showMessage(
                    message = message,
                    actionLabel = null,
                )
            }
        }
    }
    return compositionLocalProviderWithReturnValue(LocalComposeEffectErrorHandler provides handler) {
        block(userMessageStateHolder)
    }
}
