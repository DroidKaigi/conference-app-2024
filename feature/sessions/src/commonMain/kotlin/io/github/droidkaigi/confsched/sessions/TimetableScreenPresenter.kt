package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.compose.SafeLaunchedEffect
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.Filters
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableUiType
import io.github.droidkaigi.confsched.model.TimetableUiType.Grid
import io.github.droidkaigi.confsched.model.localSessionsRepository
import io.github.droidkaigi.confsched.sessions.TimetableScreenEvent.Bookmark
import io.github.droidkaigi.confsched.sessions.TimetableScreenEvent.UiTypeChange
import io.github.droidkaigi.confsched.sessions.section.TimetableGridUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableSheetUiState
import io.github.droidkaigi.confsched.ui.providePresenterDefaults
import io.github.takahirom.rin.rememberRetained
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.Flow

sealed interface TimetableScreenEvent {
    data class Bookmark(val timetableItem: TimetableItem, val bookmarked: Boolean) :
        TimetableScreenEvent

    data object UiTypeChange : TimetableScreenEvent
}

@Composable
fun timetableScreenPresenter(
    events: Flow<TimetableScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): TimetableScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val sessions by rememberUpdatedState(sessionsRepository.timetable())
    var timetableUiType by rememberRetained { mutableStateOf(TimetableUiType.List) }
    val timetableUiState by rememberUpdatedState(
        timetableSheet(
            sessionTimetable = sessions,
            uiType = timetableUiType,
        ),
    )
    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Bookmark -> {
                    sessionsRepository.toggleBookmark(event.timetableItem.id)
                }

                UiTypeChange -> {
                    timetableUiType =
                        if (timetableUiType == TimetableUiType.List) {
                            Grid
                        } else {
                            TimetableUiType.List
                        }
                }
            }
        }
    }
    TimetableScreenUiState(
        contentUiState = timetableUiState,
        timetableUiType = timetableUiType,
        userMessageStateHolder = userMessageStateHolder,
    )
}

@Composable
fun timetableSheet(
    sessionTimetable: Timetable,
    uiType: TimetableUiType,
): TimetableSheetUiState {
    if (sessionTimetable.timetableItems.isEmpty()) {
        return TimetableSheetUiState.Empty
    }
    return if (uiType == TimetableUiType.List) {
        TimetableSheetUiState.ListTimetable(
            DroidKaigi2024Day.entries.associateWith { day ->
                val sortAndGroupedTimetableItems = sessionTimetable.filtered(
                    Filters(
                        days = listOf(day),
                    ),
                ).timetableItems.groupBy {
                    it.startsTimeString to it.endsTimeString
                }.mapValues { entries ->
                    entries.value.sortedWith(
                        compareBy({ it.day?.name.orEmpty() }, { it.startsTimeString }),
                    )
                }.toPersistentMap()
                TimetableListUiState(
                    timetableItemMap = sortAndGroupedTimetableItems,
                    timetable = sessionTimetable.dayTimetable(day),
                )
            },
        )
    } else {
        TimetableSheetUiState.GridTimetable(
            DroidKaigi2024Day.entries.associateWith { day ->
                TimetableGridUiState(
                    timetable = sessionTimetable.dayTimetable(day),
                )
            },
        )
    }
}
