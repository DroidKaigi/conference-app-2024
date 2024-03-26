package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AndroidUiDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.ViewModelLifecycle
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(ViewModelComponent::class)
class KmpViewModelLifecycleModule {
    @Provides
    fun provideKmpViewModelLifecycle(viewModelLifecycle: ViewModelLifecycle): KmpViewModelLifecycle {
        return KmpViewModelLifecycle().also {
            viewModelLifecycle.addOnClearedListener { it.onCleared() }
        }
    }
}

fun <Event : Any, UiState : Any> ComposeViewModel(
    viewModelLifecycle: KmpViewModelLifecycle,
    userMessageStateHolder: UserMessageStateHolder,
    content: @Composable ComposeViewModel<Event, UiState>.(Flow<Event>) -> UiState,
): ComposeViewModel<Event, UiState> {
    val kmpViewModelLifecycle = KmpViewModelLifecycle()
    viewModelLifecycle.addOnClearedListener { kmpViewModelLifecycle.onCleared() }
    return DefaultComposeViewModel(
        viewModelLifecycle = kmpViewModelLifecycle,
        composeCoroutineContext = AndroidUiDispatcher.Main,
        userMessageStateHolder = userMessageStateHolder,
        content = content,
    )
}
