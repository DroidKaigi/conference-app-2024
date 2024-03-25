package io.github.droidkaigi.confsched.contributors

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched.model.ContributorsRepository
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import javax.inject.Inject

@HiltViewModel
class AndroidContributorsViewModel @Inject constructor(
    contributorsRepository: ContributorsRepository,
    userMessageStateHolder: UserMessageStateHolder,
) : ContributorsViewModel(contributorsRepository, userMessageStateHolder)
