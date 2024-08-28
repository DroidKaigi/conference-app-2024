package io.github.droidkaigi.confsched.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.FontFamily
import io.github.droidkaigi.confsched.model.Settings
import io.github.droidkaigi.confsched.model.SettingsRepository
import io.github.droidkaigi.confsched.model.localSettingsRepository
import io.github.droidkaigi.confsched.settings.SettingsScreenEvent.SelectUseFontFamily

sealed interface SettingsScreenEvent {
    data class SelectUseFontFamily(val fontFamily: FontFamily) : SettingsScreenEvent
}

@Composable
fun settingsScreenPresenter(
    events: EventFlow<SettingsScreenEvent>,
    settingsRepository: SettingsRepository = localSettingsRepository(),
): SettingsUiState = providePresenterDefaults { userMessageStateHolder ->
    val settings by rememberUpdatedState(settingsRepository.settings())

    EventEffect(events) { event ->
        when (event) {
            is SelectUseFontFamily -> settingsRepository.save(
                settings = Settings.Exists(
                    useFontFamily = event.fontFamily,
                ),
            )
        }
    }
    SettingsUiState(
        useFontFamily = when (settings) {
            Settings.DoesNotExists -> FontFamily.DotGothic16Regular
            is Settings.Exists -> (settings as Settings.Exists).useFontFamily
            Settings.Loading -> null
        },
        userMessageStateHolder = userMessageStateHolder,
    )
}
