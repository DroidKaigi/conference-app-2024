package io.github.droidkaigi.confsched.main

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface MainScreenEvent

@Composable
fun mainScreenPresenter(
    @Suppress("UnusedParameter")
    events: Flow<MainScreenEvent>,
): MainScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    MainScreenUiState(
        userMessageStateHolder = userMessageStateHolder,
    )
}
