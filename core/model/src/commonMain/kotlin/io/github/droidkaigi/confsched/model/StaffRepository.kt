package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

interface StaffRepository {

    public fun staffs(): Flow<PersistentList<Staff>>

    @Throws(AppError::class, CancellationException::class)
    public suspend fun refresh()

    @Composable
    fun staff(): PersistentList<Staff>
}

@Composable
fun localStaffRepository(): StaffRepository {
    return LocalRepositories.current[StaffRepository::class] as StaffRepository
}
