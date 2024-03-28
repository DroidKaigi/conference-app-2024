package io.github.droidkaigi.confsched.contributors

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.PausableMonotonicFrameClock
import androidx.compose.runtime.monotonicFrameClock
import androidx.compose.ui.platform.AndroidUiDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.ui.KmpViewModelLifecycle
import javax.inject.Inject

@OptIn(ExperimentalComposeApi::class)
@HiltViewModel
class AndroidContributorsViewModel @Inject constructor(
    contributorsRepository: ContributorsRepository,
    viewModelLifecycle: KmpViewModelLifecycle,
) : ContributorsScreenViewModel(
    contributorsRepository = contributorsRepository,
    composeCoroutineContext = AndroidUiDispatcher.Main + PausableMonotonicFrameClock(AndroidUiDispatcher.Main.monotonicFrameClock),
    viewModelLifecycle = viewModelLifecycle
)
