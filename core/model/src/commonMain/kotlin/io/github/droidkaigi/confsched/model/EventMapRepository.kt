package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow

interface EventMapRepository {

    suspend fun refresh()

    @Composable
    fun eventMapEvents(): PersistentList<EventMapEvent>

    fun getEventMapStream(): Flow<PersistentList<EventMapEvent>>
}

@Composable
fun localEventMapRepository(): EventMapRepository {
    return LocalRepositories.current[EventMapRepository::class] as EventMapRepository
}