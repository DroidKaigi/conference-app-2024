package io.github.droidkaigi.confsched.about

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.AboutRepository
import io.github.droidkaigi.confsched.model.localAboutRepository

@Composable
fun aboutScreenPresenter(
    aboutRepository: AboutRepository = localAboutRepository(),
): AboutUiState = providePresenterDefaults { _ ->
    AboutUiState(
        versionName = aboutRepository.versionName(),
    )
}
