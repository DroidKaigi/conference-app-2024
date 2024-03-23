package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AndroidUiDispatcher
import dagger.hilt.android.ViewModelLifecycle
import kotlinx.coroutines.flow.Flow

fun <Event : Any, UiState : Any> ComposeViewModel(
    viewModelLifecycle: ViewModelLifecycle,
    userMessageStateHolder: UserMessageStateHolder,
    content: @Composable ComposeViewModel<Event, UiState>.(Flow<Event>) -> UiState,
): ComposeViewModel<Event, UiState> {

    val kmpViewModelLifecycle = KmpViewModelLifecycle()
    viewModelLifecycle.addOnClearedListener { kmpViewModelLifecycle.onCleared() }
    return DefaultComposeViewModel(
        viewModelLifecycle = kmpViewModelLifecycle,
        composeCoroutineContext = AndroidUiDispatcher.Main,
        userMessageStateHolder = userMessageStateHolder,
        content = content
    )
}
