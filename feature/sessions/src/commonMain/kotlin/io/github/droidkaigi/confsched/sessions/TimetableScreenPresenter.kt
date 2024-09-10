package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import io.github.droidkaigi.confsched.compose.EventEffect
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalClock
import io.github.droidkaigi.confsched.droidkaigiui.providePresenterDefaults
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.Filters
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.TimeLine
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableUiType
import io.github.droidkaigi.confsched.model.TimetableUiType.Grid
import io.github.droidkaigi.confsched.model.localSessionsRepository
import io.github.droidkaigi.confsched.sessions.TimetableScreenEvent.Bookmark
import io.github.droidkaigi.confsched.sessions.TimetableScreenEvent.UiTypeChange
import io.github.droidkaigi.confsched.sessions.section.TimetableGridUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableUiState
import io.github.takahirom.rin.rememberRetained
import kotlinx.collections.immutable.toPersistentMap

sealed interface TimetableScreenEvent {
    data class Bookmark(val timetableItem: TimetableItem, val bookmarked: Boolean) :
        TimetableScreenEvent

    data object UiTypeChange : TimetableScreenEvent
}

@Composable
fun timetableScreenPresenter(
    events: EventFlow<TimetableScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): TimetableScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    val sessions by rememberUpdatedState(sessionsRepository.timetable())
    var timetableUiType by rememberRetained { mutableStateOf(TimetableUiType.List) }

    val clock = LocalClock.current
    var timeLine by remember { mutableStateOf(TimeLine.now(clock)) }

    val timetableUiState by rememberUpdatedState(
        timetableSheet(
            sessionTimetable = sessions,
            uiType = timetableUiType,
            timeLine = timeLine,
        ),
    )

    EventEffect(events) { event ->
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

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        timeLine = TimeLine.now(clock)
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
    timeLine: TimeLine?,
): TimetableUiState {
    if (sessionTimetable.timetableItems.isEmpty()) {
        return TimetableUiState.Empty
    }
    return if (uiType == TimetableUiType.List) {
        TimetableUiState.ListTimetable(
            DroidKaigi2024Day.visibleDays().associateWith { day ->
                val sortAndGroupedTimetableItems = sessionTimetable.filtered(
                    Filters(
                        days = listOf(day),
                    ),
                ).timetableItems.groupBy {
                    TimetableListUiState.TimeSlot(
                        startTimeString = it.startsTimeString,
                        endTimeString = it.endsTimeString,
                    )
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
        TimetableUiState.GridTimetable(
            timetableGridUiState = DroidKaigi2024Day.visibleDays().associateWith { day ->
                TimetableGridUiState(
                    timetable = sessionTimetable.dayTimetable(day),
                )
            },
            timeLine = timeLine,
        )
    }
}
