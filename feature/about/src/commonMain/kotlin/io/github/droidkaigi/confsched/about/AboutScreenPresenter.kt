package io.github.droidkaigi.confsched.about

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.AboutRepository
import io.github.droidkaigi.confsched.model.localAboutRepository
import io.github.droidkaigi.confsched.ui.providePresenterDefaults

@Composable
fun aboutScreenPresenter(
    aboutRepository: AboutRepository = localAboutRepository(),
): AboutUiState = providePresenterDefaults { _ ->
    AboutUiState(
        versionName = aboutRepository.versionName(),
    )
}
