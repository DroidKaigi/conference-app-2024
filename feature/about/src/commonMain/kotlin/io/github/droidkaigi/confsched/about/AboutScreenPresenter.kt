package io.github.droidkaigi.confsched.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.model.AboutRepository
import io.github.droidkaigi.confsched.model.Settings.Exists
import io.github.droidkaigi.confsched.model.Settings.Loading
import io.github.droidkaigi.confsched.model.SettingsRepository
import io.github.droidkaigi.confsched.model.localAboutRepository
import io.github.droidkaigi.confsched.model.localSettingsRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults

@Composable
fun aboutScreenPresenter(
    aboutRepository: AboutRepository = localAboutRepository(),
    settingsRepository: SettingsRepository = localSettingsRepository(),
): AboutUiState = providePresenterDefaults { _ ->
    val settings by rememberUpdatedState(settingsRepository.settings())

    if (settings is Loading) return@providePresenterDefaults AboutUiState.Loading
    AboutUiState.Loaded(
        versionName = aboutRepository.versionName(),
        enableAnimation = (settings as Exists).enableAnimation,
        enableFallbackMode = (settings as Exists).enableFallbackMode,
    )
}
