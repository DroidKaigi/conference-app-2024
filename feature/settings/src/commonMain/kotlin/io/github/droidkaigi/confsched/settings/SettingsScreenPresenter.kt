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
import io.github.droidkaigi.confsched.settings.SettingsScreenEvent.SelectEnableFallbackMode
import io.github.droidkaigi.confsched.settings.SettingsScreenEvent.SelectUseFontFamily
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface SettingsScreenEvent {
    data class SelectUseFontFamily(val fontFamily: FontFamily) : SettingsScreenEvent
    data class SelectEnableAnimation(val enableAnimation: Boolean) : SettingsScreenEvent
    data class SelectEnableFallbackMode(val enableFallbackMode: Boolean) : SettingsScreenEvent
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
                        enableAnimation = (settings as Settings.Exists).enableAnimation,
                        enableFallbackMode = (settings as Settings.Exists).enableFallbackMode,
                    ),
                )

                is SelectEnableAnimation -> settingsRepository.save(
                    settings = Settings.Exists(
                        useFontFamily = (settings as Settings.Exists).useFontFamily,
                        enableAnimation = event.enableAnimation,
                        enableFallbackMode = (settings as Settings.Exists).enableFallbackMode,
                    ),
                )

                is SelectEnableFallbackMode -> settingsRepository.save(
                    settings = Settings.Exists(
                        useFontFamily = (settings as Settings.Exists).useFontFamily,
                        // There should be no need to animate on a device that has to Fallback.
                        enableAnimation = false,
                        enableFallbackMode = event.enableFallbackMode,
                    ),
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
            is Settings.Exists -> (settings as Settings.Exists).enableAnimation
            Settings.Loading -> true
        },
        enableFallbackMode = when (settings) {
            is Settings.Exists -> (settings as Settings.Exists).enableFallbackMode
            Settings.Loading -> false
        },
        userMessageStateHolder = userMessageStateHolder,
    )
}
