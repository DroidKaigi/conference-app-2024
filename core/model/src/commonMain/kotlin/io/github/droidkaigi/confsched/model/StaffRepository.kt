package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

public sealed interface StreamResponse<out D> {
    public data class Success<D>(val data: D) : StreamResponse<D>
    public data class Failure(val e: Throwable) : StreamResponse<Nothing>
}

interface StaffRepository {

    public fun staffs(): Flow<StreamResponse<PersistentList<Staff>>>

    @Throws(CancellationException::class)
    public suspend fun refresh()

    @Composable
    fun staff(): PersistentList<Staff>
}

@Composable
fun localStaffRepository(): StaffRepository {
    return LocalRepositories.current[StaffRepository::class] as StaffRepository
}
