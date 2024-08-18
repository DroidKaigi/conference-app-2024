package io.github.droidkaigi.confsched.settings

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface SettingsScreenEvent

@Composable
fun settingsScreenPresenter(
    events: Flow<SettingsScreenEvent>,
//    settingsRepository: SettingsRepository = localSettingsRepository(), // TODO Implement SettingsRepository
): SettingsUiState = providePresenterDefaults { userMessageStateHolder ->
    SafeLaunchedEffect(Unit) {
        events.collect {}
    }
    SettingsUiState(
        userMessageStateHolder = userMessageStateHolder,
    )
}
