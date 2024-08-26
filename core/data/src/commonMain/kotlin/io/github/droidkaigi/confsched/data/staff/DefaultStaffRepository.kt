package io.github.droidkaigi.confsched.data.staff

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.model.Staff
import io.github.droidkaigi.confsched.model.StaffRepository
import io.github.droidkaigi.confsched.model.StreamResponse
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform

public class DefaultStaffRepository(
    private val staffApi: StaffApiClient,
) : StaffRepository {

    private val staffsStateFlow = MutableStateFlow<PersistentList<Staff>>(persistentListOf())

    override fun staffs(): Flow<StreamResponse<PersistentList<Staff>>> {
        return staffsStateFlow.onStart {
            if (staffsStateFlow.value.isEmpty()) {
                refresh()
            }
        }
        .transform {
            emit(StreamResponse.Success(it))
        }
        .catch<StreamResponse<PersistentList<Staff>>> {
            emit(StreamResponse.Failure(it))
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
