package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import conference_app_2024.core.ui.generated.resources.connection_failed
import org.jetbrains.compose.resources.stringResource

/**
 * If you want to display composeResource error messages instead of error messages from the API, define them here
 * [Throwable.toApplicationErrorMessage] also needs to be defined
 */
@Composable
internal fun ComposeResourceErrorMessages(): List<ComposeResourceErrorMessage> = listOf(
    ComposeResourceErrorMessage(
        ComposeResourceErrorMessageType.ConnectionFailed,
        stringResource(UiRes.string.connection_failed),
    ),
)

internal enum class ComposeResourceErrorMessageType {
    ConnectionFailed,
}

internal data class ComposeResourceErrorMessage(
    val type: ComposeResourceErrorMessageType,
    val message: String,
)
