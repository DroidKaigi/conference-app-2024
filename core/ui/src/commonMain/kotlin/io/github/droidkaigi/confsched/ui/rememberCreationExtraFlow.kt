package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.flow.StateFlow

class CreationExtraViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {
}

@Composable
fun <T> rememberCreationExtraFlow(key: String, initialValue: T): StateFlow<T> {
    val viewModel = viewModel<CreationExtraViewModel>()
    return rememberRetained {
        val creationExtras: SavedStateHandle = viewModel.savedStateHandle
        creationExtras.getStateFlow(key, initialValue)
    }
}
