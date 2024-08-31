package io.github.droidkaigi.confsched.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.UserMessageResult.ActionPerformed
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.shared.IosComposeKaigiAppEvent.SettingsAppNavigated
import io.github.droidkaigi.confsched.shared.IosComposeKaigiAppEvent.ShowRequiresAuthorization

sealed interface IosComposeKaigiAppEvent {
    data class ShowRequiresAuthorization(
        val snackbarMessage: String,
        val actionLabel: String,
    ) : IosComposeKaigiAppEvent

    data object SettingsAppNavigated : IosComposeKaigiAppEvent
}

@Composable
fun iosComposeKaigiAppPresenter(
    events: EventFlow<IosComposeKaigiAppEvent>
) : IosComposeKaigiAppUiState = providePresenterDefaults { userMessageStateHolder ->
    var shouldGoToSettingsApp by remember { mutableStateOf(false) }

    EventEffect(events) { event ->
        when (event) {
            is ShowRequiresAuthorization -> {
                val result = userMessageStateHolder.showMessage(
                    message = event.snackbarMessage,
                    actionLabel = event.actionLabel,
                )
                if (result == ActionPerformed) {
                    shouldGoToSettingsApp = true
                }
            }

            SettingsAppNavigated -> {
                shouldGoToSettingsApp = false
            }
        }
    }
    IosComposeKaigiAppUiState(
        userMessageStateHolder = userMessageStateHolder,
        shouldGoToSettingsApp = shouldGoToSettingsApp,
    )
}
