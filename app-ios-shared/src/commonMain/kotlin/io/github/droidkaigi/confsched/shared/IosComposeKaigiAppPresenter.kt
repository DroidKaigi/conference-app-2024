package io.github.droidkaigi.confsched.shared

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.shared.IosComposeKaigiAppEvent.ShowRequiresAuthorization

sealed interface IosComposeKaigiAppEvent {
    data class ShowRequiresAuthorization(
        val snackbarMessage: String,
        val actionLabel: String,
    ) : IosComposeKaigiAppEvent
}

@Composable
fun iosComposeKaigiAppPresenter(
    events: EventFlow<IosComposeKaigiAppEvent>
) : IosComposeKaigiAppUiState = providePresenterDefaults { userMessageStateHolder ->
    EventEffect(events) { event ->
        when (event) {
            is ShowRequiresAuthorization -> {
                userMessageStateHolder.showMessage(
                    message = event.snackbarMessage,
                    // TODO Add code to transition to the settings screen when the action button is pressed.
                    // TODO Perhaps UIApplication.openSettingsURLString can be used to achieve this.
                    actionLabel = event.actionLabel,
                )
            }
        }
    }
    IosComposeKaigiAppUiState(userMessageStateHolder)
}
