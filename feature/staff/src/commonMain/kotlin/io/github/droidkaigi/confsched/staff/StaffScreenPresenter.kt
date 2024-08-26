package io.github.droidkaigi.confsched.staff

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.StaffRepository
import io.github.droidkaigi.confsched.model.localStaffRepository

sealed interface StaffScreenEvent

@Composable
fun staffScreenPresenter(
    events: EventFlow<StaffScreenEvent>,
    staffRepository: StaffRepository = localStaffRepository(),
): StaffUiState = providePresenterDefaults { userMessageStateHolder ->
    val staff by rememberUpdatedState(staffRepository.staff())
    EventEffect(events) { event ->
    }
    StaffUiState(
        staff = staff,
        userMessageStateHolder = userMessageStateHolder,
    )
}
