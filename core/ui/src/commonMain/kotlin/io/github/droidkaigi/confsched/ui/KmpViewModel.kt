package io.github.droidkaigi.confsched.ui

import kotlinx.coroutines.CoroutineScope

expect abstract class KmpViewModel()

expect val KmpViewModel.viewModelScope: CoroutineScope
