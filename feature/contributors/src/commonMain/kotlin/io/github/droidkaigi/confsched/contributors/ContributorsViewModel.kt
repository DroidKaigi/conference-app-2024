package io.github.droidkaigi.confsched.contributors

import io.github.droidkaigi.confsched.designsystem.strings.AppStrings
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.ui.HiltViewModel
import io.github.droidkaigi.confsched.ui.Inject
import io.github.droidkaigi.confsched.ui.KmpViewModel
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.buildUiState
import io.github.droidkaigi.confsched.ui.handleErrorAndRetry
import io.github.droidkaigi.confsched.ui.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ContributorsViewModel @Inject constructor(
    contributorsRepository: ContributorsRepository,
    val userMessageStateHolder: UserMessageStateHolder,
) : KmpViewModel(), UserMessageStateHolder by userMessageStateHolder {
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
