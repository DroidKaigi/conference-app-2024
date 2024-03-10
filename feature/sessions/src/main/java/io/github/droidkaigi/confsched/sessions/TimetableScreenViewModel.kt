package io.github.droidkaigi.confsched.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.droidkaigi.confsched.designsystem.strings.AppStrings
import io.github.droidkaigi.confsched.model.DroidKaigi2023Day
import io.github.droidkaigi.confsched.model.Filters
import io.github.droidkaigi.confsched.model.SessionsRepository
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableUiType
import io.github.droidkaigi.confsched.sessions.section.TimetableGridUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState
import io.github.droidkaigi.confsched.sessions.section.TimetableSheetUiState
import io.github.droidkaigi.confsched.ui.UserMessageStateHolder
import io.github.droidkaigi.confsched.ui.buildUiState
import io.github.droidkaigi.confsched.ui.handleErrorAndRetry
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableScreenViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    val userMessageStateHolder: UserMessageStateHolder,
) : ViewModel(),
    UserMessageStateHolder by userMessageStateHolder {
    private val sessionsStateFlow: StateFlow<Timetable> = sessionsRepository
        .getTimetableStream()
        .handleErrorAndRetry(
            AppStrings.Retry,
            userMessageStateHolder,
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Timetable(),
        )
    private val timetableUiTypeStateFlow: MutableStateFlow<TimetableUiType> =
        MutableStateFlow(TimetableUiType.List)

    private val bookmarkAnimationStartStateFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    private val timetableSheetUiState: StateFlow<TimetableSheetUiState> = buildUiState(
        sessionsStateFlow,
        timetableUiTypeStateFlow,
    ) { sessionTimetable, uiType ->
        if (sessionTimetable.timetableItems.isEmpty()) {
            return@buildUiState TimetableSheetUiState.Empty
        }
        if (uiType == TimetableUiType.List) {
            TimetableSheetUiState.ListTimetable(
                DroidKaigi2023Day.entries.associateWith { day ->
                    val sortAndGroupedTimetableItems = sessionTimetable.filtered(
                        Filters(
                            days = listOf(day),
                        ),
                    ).timetableItems.groupBy {
                        it.startsTimeString + it.endsTimeString
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
                DroidKaigi2023Day.entries.associateWith { day ->
                    TimetableGridUiState(
                        timetable = sessionTimetable.dayTimetable(day),
                    )
                },
            )
        }
    }

    val uiState: StateFlow<TimetableScreenUiState> = buildUiState(
        timetableSheetUiState,
        bookmarkAnimationStartStateFlow,
    ) { sessionListUiState, _ ->
        TimetableScreenUiState(
            contentUiState = sessionListUiState,
            timetableUiType = timetableUiTypeStateFlow.value,
            onBookmarkIconClickStatus = bookmarkAnimationStartStateFlow.value,
        )
    }

    fun onUiTypeChange() {
        timetableUiTypeStateFlow.value =
            if (timetableUiTypeStateFlow.value == TimetableUiType.List) {
                TimetableUiType.Grid
            } else {
                TimetableUiType.List
            }
    }

    fun onBookmarkClick(
        session: TimetableItem,
        isBookmarked: Boolean,
    ) {
        viewModelScope.launch {
            sessionsRepository.toggleBookmark(session.id)
        }
        if (isBookmarked) {
            bookmarkAnimationStartStateFlow.value = true
        }
    }

    fun onReachAnimationEnd() {
        bookmarkAnimationStartStateFlow.value = false
    }
}
