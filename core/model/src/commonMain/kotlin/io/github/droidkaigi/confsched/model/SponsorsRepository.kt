package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

public interface SponsorsRepository {
    public fun getSponsorStream(): Flow<PersistentList<Sponsor>>

    @Throws(CancellationException::class)
    public suspend fun refresh()

    @Composable
    fun sponsors(): PersistentList<Sponsor>
}
