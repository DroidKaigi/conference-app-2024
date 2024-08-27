package io.github.droidkaigi.confsched.data.staff

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.Staff
import io.github.droidkaigi.confsched.model.StaffRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

public class DefaultStaffRepository(
    private val staffApi: StaffApiClient,
) : StaffRepository {

    private val staffsStateFlow = MutableStateFlow<PersistentList<Staff>>(persistentListOf())

    override fun staffs(): Flow<PersistentList<Staff>> {
        return staffsStateFlow.onStart {
            if (staffsStateFlow.value.isEmpty()) {
                refresh()
            }
        }
            .catch {
                // SKIE doesn't support throwing exceptions from Flow.
                // For more information, please refer to https://github.com/touchlab/SKIE/discussions/19 .
                Logger.e("Failed to refresh in staffs()", it)
                emit(staffsStateFlow.value)
            }
    }

    override suspend fun refresh() {
        staffsStateFlow.value = staffApi
            .getStaff()
            .toPersistentList()
    }

    @Composable
    override fun staff(): PersistentList<Staff> {
        val staff by staffsStateFlow.safeCollectAsRetainedState()
        SafeLaunchedEffect(Unit) {
            if (staff.isEmpty()) {
                refresh()
            }
        }
        return staff
    }
}
