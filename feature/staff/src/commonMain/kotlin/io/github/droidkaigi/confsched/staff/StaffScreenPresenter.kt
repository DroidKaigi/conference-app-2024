package io.github.droidkaigi.confsched.staff

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.StaffRepository
import io.github.droidkaigi.confsched.model.localStaffRepository
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import kotlinx.coroutines.flow.Flow

sealed interface StaffScreenEvent

@Composable
fun staffScreenPresenter(
    events: Flow<StaffScreenEvent>,
    staffRepository: StaffRepository = localStaffRepository(),
): StaffUiState = providePresenterDefaults { userMessageStateHolder ->
    val staff by rememberUpdatedState(staffRepository.staff())
    SafeLaunchedEffect(Unit) {
        events.collect {}
    }
    StaffUiState(
        staff = staff,
        userMessageStateHolder = userMessageStateHolder,
    )
}
