package io.github.droidkaigi.confsched.contributors

import androidx.compose.ui.platform.AndroidUiDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.ui.KmpViewModelLifecycle
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import javax.inject.Inject

@HiltViewModel
class AndroidContributorsViewModel @Inject constructor(
    contributorsRepository: ContributorsRepository,
    userMessageStateHolder: UserMessageStateHolder,
    viewModelLifecycle: KmpViewModelLifecycle,
) : ContributorsScreenViewModel(
    contributorsRepository = contributorsRepository,
    userMessageStateHolder = userMessageStateHolder,
    composeCoroutineContext = AndroidUiDispatcher.Main,
    viewModelLifecycle = viewModelLifecycle
)
