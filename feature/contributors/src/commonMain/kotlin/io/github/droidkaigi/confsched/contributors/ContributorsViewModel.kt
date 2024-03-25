package io.github.droidkaigi.confsched.contributors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.droidkaigi.confsched.designsystem.strings.AppStrings
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.buildUiState
import io.github.droidkaigi.confsched.ui.handleErrorAndRetry
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

open class ContributorsViewModel(
    contributorsRepository: ContributorsRepository,
    val userMessageStateHolder: UserMessageStateHolder,
) : ViewModel(), UserMessageStateHolder by userMessageStateHolder {
    private val contributors = contributorsRepository
        .contributors()
        .handleErrorAndRetry(
            AppStrings.Retry,
            userMessageStateHolder,
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = persistentListOf()
        )

    val uiState: StateFlow<ContributorsUiState> = buildUiState(contributors) {
        ContributorsUiState(
            contributors = it,
        )
    }
}
