package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.droidkaigi.confsched.compose.ComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.CompositionLocalProviderWithReturnValue
import io.github.droidkaigi.confsched.compose.LocalComposeEffectErrorHandler

@Composable
fun <T> providePresenterDefaults(
    userMessageStateHolder: UserMessageStateHolder = rememberUserMessageStateHolder(),
    block: @Composable (UserMessageStateHolder) -> T,
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
        block(userMessageStateHolder)
    }
}
