package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import io.github.droidkaigi.confsched.compose.ComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.LocalComposeEffectErrorHandler
import io.github.droidkaigi.confsched.compose.compositionLocalProviderWithReturnValue

@Composable
fun <T> providePresenterDefaults(
    userMessageStateHolder: UserMessageStateHolder = rememberUserMessageStateHolder(),
    block: @Composable (UserMessageStateHolder) -> T,
): T {
    var composeResourceErrorMessages: List<ComposeResourceErrorMessage> = listOf()
    // For iOS
    CompositionLocalProvider(LocalDensity provides Density(1F)) {
        composeResourceErrorMessages = composeResourceErrorMessages()
    }
    val handler = remember(userMessageStateHolder) {
        object : ComposeEffectErrorHandler {
            override suspend fun emit(throwable: Throwable) {
                val message = throwable.toApplicationErrorMessage(composeResourceErrorMessages)
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
