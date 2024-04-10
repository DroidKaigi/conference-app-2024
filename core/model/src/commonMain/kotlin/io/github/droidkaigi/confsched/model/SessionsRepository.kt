package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import kotlinx.coroutines.flow.Flow

interface SessionsRepository {
    fun getTimetableStream(): Flow<Timetable>
    fun getTimetableItemWithBookmarkStream(id: TimetableItemId): Flow<Pair<TimetableItem, Boolean>>

    @Composable
    fun timetable(): Timetable

    @Composable
    fun timetableItemWithBookmark(id: TimetableItemId): Pair<TimetableItem, Boolean>?
    suspend fun toggleBookmark(id: TimetableItemId)
}

@Composable
fun localSessionsRepository(): SessionsRepository {
    return LocalRepositories.current[SessionsRepository::class] as SessionsRepository
}
