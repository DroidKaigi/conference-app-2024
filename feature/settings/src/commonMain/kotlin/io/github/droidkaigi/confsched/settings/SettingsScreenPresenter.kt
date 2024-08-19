package io.github.droidkaigi.confsched.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.FontFamily
import io.github.droidkaigi.confsched.model.Settings
import io.github.droidkaigi.confsched.model.SettingsRepository
import io.github.droidkaigi.confsched.model.localSettingsRepository
import io.github.droidkaigi.confsched.settings.SettingsScreenEvent.SelectEnableAnimation
import io.github.droidkaigi.confsched.settings.SettingsScreenEvent.SelectUseFontFamily
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface SettingsScreenEvent {
    data class SelectUseFontFamily(val fontFamily: FontFamily) : SettingsScreenEvent
    data class SelectEnableAnimation(val enableAnimation: Boolean) : SettingsScreenEvent
}

@Composable
fun settingsScreenPresenter(
    events: Flow<SettingsScreenEvent>,
    settingsRepository: SettingsRepository = localSettingsRepository(),
): SettingsUiState = providePresenterDefaults { userMessageStateHolder ->
    val settings by rememberUpdatedState(settingsRepository.settings())

    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is SelectUseFontFamily -> settingsRepository.save(
                    settings = Settings.Exists(
                        useFontFamily = event.fontFamily,
                        enableAnimation = (settings as Settings.Exists).enableAnimation
                    ),
                )

                is SelectEnableAnimation -> settingsRepository.save(
                    settings = Settings.Exists(
                        useFontFamily = (settings as Settings.Exists).useFontFamily,
                        enableAnimation = event.enableAnimation
                    )
                )
            }
        }
    }
    SettingsUiState(
        useFontFamily = when (settings) {
            is Settings.Exists -> (settings as Settings.Exists).useFontFamily
            Settings.Loading -> null
        },
        enableAnimation = when (settings) {
            Settings.Loading -> true
            is Settings.Exists -> (settings as Settings.Exists).enableAnimation
        },
        userMessageStateHolder = userMessageStateHolder,
    )
}
