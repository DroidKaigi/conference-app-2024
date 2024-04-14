package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.takahirom.rin.collectAsRetainedState

class CreationExtraViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {
}

@Composable
fun <T : Any> rememberCreationExtra(key: String, initialValue: T): T {
    val viewModel = viewModel(CreationExtraViewModel::class)
    val creationExtras: SavedStateHandle = viewModel.savedStateHandle
    val extra by creationExtras.getStateFlow(key, initialValue).collectAsRetainedState()
    return extra
}
