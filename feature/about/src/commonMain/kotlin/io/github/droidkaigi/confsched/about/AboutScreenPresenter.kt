package io.github.droidkaigi.confsched.about

import androidx.compose.runtime.Composable
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
    val enableAnimation = when (val settings = settingsRepository.settings()) {
        Loading -> true
        is Exists -> settings.enableAnimation
    }

    AboutUiState(
        versionName = aboutRepository.versionName(),
        enableAnimation = enableAnimation
    )
}
