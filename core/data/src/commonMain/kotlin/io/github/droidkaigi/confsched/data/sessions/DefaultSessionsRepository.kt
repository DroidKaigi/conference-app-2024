package io.github.droidkaigi.confsched.data.sessions

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.compose.safeCollectAsRetainedState
import io.github.droidkaigi.confsched.data.sessions.response.SessionsAllResponse
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItemId
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.coroutineContext

public class DefaultSessionsRepository(
    private val sessionsApi: SessionsApiClient,
    private val userDataStore: UserDataStore,
    private val sessionCacheDataStore: SessionCacheDataStore,
) : SessionsRepository {

    override fun getTimetableStream(): Flow<Timetable> = flow {
        var first = true
        combine(
            sessionCacheDataStore.getTimetableStream().catch { e ->
                Logger.d(
                    "DefaultSessionsRepository sessionCacheDataStore.getTimetableStream catch",
                    e,
                )
                refreshSessionData()
                emitAll(sessionCacheDataStore.getTimetableStream())
            },
            userDataStore.getFavoriteSessionStream(),
        ) { timetable, favorites ->
            timetable.copy(bookmarks = favorites)
        }
            .catch {
                // SKIE doesn't support throwing exceptions from Flow.
                // For more information, please refer to https://github.com/touchlab/SKIE/discussions/19 .
                Logger.e("Failed to refresh in getTimetableStream()", it)
                emit(Timetable())
            }
            .collect {
                if (!it.isEmpty()) {
                    emit(it)
                }
                if (first) {
                    first = false
                    Logger.d("DefaultSessionsRepository onStart getTimetableStream()")
                    try {
                        sessionCacheDataStore.save(sessionsApi.sessionsAllResponse())
                    } catch (e: Exception) {
                        // SKIE doesn't support throwing exceptions from Flow.
                        // For more information, please refer to https://github.com/touchlab/SKIE/discussions/19 .
                        coroutineContext.ensureActive()
                        Logger.e("Failed to refresh Timetable in getTimetableStream()", e)
                    }
                    Logger.d("DefaultSessionsRepository onStart fetched")
                }
            }
    }

    private suspend fun refreshSessionData() {
        val sessionsAllResponse = sessionsApi.sessionsAllResponse()
        // Remove workday sessions
        val sessionsAllResponseFiltered = sessionsAllResponse.filterConferenceDaySessions()
        sessionCacheDataStore.save(sessionsAllResponseFiltered)
    }

    override fun getTimetableItemWithBookmarkStream(id: TimetableItemId): Flow<Pair<TimetableItem, Boolean>> {
        return getTimetableStream().map { timetable ->
            timetable.timetableItems.first { it.id == id } to timetable.bookmarks.contains(id)
        }
    }

    @Composable
    public override fun timetable(): Timetable {
        var first by remember { mutableStateOf(true) }
        SafeLaunchedEffect(first) {
            if (first) {
                Logger.d("DefaultSessionsRepository onStart getTimetableStream()")
                refreshSessionData()
                Logger.d("DefaultSessionsRepository onStart fetched")
                first = false
            }
        }

        val timetable by remember {
            sessionCacheDataStore.getTimetableStream().catch { e ->
                Logger.d(
                    "DefaultSessionsRepository sessionCacheDataStore.getTimetableStream catch",
                    e,
                )
                sessionCacheDataStore.save(sessionsApi.sessionsAllResponse())
                emitAll(sessionCacheDataStore.getTimetableStream())
            }
        }.safeCollectAsRetainedState(Timetable())
        val favoriteSessions by remember {
            userDataStore.getFavoriteSessionStream()
        }.safeCollectAsRetainedState(persistentSetOf())

        Logger.d { "DefaultSessionsRepository timetable() count=${timetable.timetableItems.size}" }
        return timetable.copy(bookmarks = favoriteSessions)
    }

    @Composable
    override fun timetableItemWithBookmark(id: TimetableItemId): Pair<TimetableItem, Boolean>? {
        val timetable by rememberUpdatedState(timetable())
        val itemWithBookmark = remember(id, timetable) {
            val timetableItem =
                timetable.timetableItems.firstOrNull { it.id == id } ?: return@remember null
            timetableItem to timetable.bookmarks.contains(id)
        }
        Logger.d {
            "DefaultSessionsRepository timetableItemWithBookmark() timetable size:${timetable.timetableItems.size} id:$id itemWithBookmark=${itemWithBookmark?.first?.id} ${itemWithBookmark?.second}"
        }
        return itemWithBookmark
    }

    override suspend fun toggleBookmark(id: TimetableItemId) {
        Logger.d { "DefaultSessionsRepository toggleBookmark() start id:$id" }
        userDataStore.toggleFavorite(id)
        Logger.d { "DefaultSessionsRepository toggleBookmark() end id:$id" }
    }

    public companion object {
        @VisibleForTesting
        public fun SessionsAllResponse.filterConferenceDaySessions(): SessionsAllResponse {
            return copy(
                sessions = sessions.filter {
                    val startsAt = it.startsAt.toInstantAsJST()
                    DroidKaigi2024Day.visibleDays()
                        .any { day -> day.start <= startsAt && startsAt < day.end }
                },
            )
        }
    }
}
