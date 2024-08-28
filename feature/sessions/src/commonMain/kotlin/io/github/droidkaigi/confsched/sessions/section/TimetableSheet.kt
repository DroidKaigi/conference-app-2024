package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalClock
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.TimeLine
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.sessions.component.TimetableDayTab
import io.github.droidkaigi.confsched.sessions.section.TimetableUiState.Empty
import io.github.droidkaigi.confsched.sessions.section.TimetableUiState.GridTimetable
import io.github.droidkaigi.confsched.sessions.section.TimetableUiState.ListTimetable

const val TimetableTabTestTag = "TimetableTab"

sealed interface TimetableUiState {
    data object Empty : TimetableUiState
    data class ListTimetable(
        val timetableListUiStates: Map<DroidKaigi2024Day, TimetableListUiState>,
    ) : TimetableUiState

    data class GridTimetable(
        val timetableGridUiState: Map<DroidKaigi2024Day, TimetableGridUiState>,
        val timeLine: TimeLine?,
    ) : TimetableUiState
}

@Composable
fun Timetable(
    uiState: TimetableUiState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onFavoriteClick: (TimetableItem, Boolean) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val clock = LocalClock.current
    var selectedDay by rememberSaveable { mutableStateOf(DroidKaigi2024Day.initialSelectedTabDay(clock)) }
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = modifier.padding(contentPadding.calculateTopPadding()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            TimetableDayTab(
                selectedDay = selectedDay,
                onDaySelected = { day ->
                    selectedDay = day
                },
            )
            when (uiState) {
                is ListTimetable -> {
                    val scrollStates = rememberListTimetableScrollStates()
                    TimetableList(
                        uiState = requireNotNull(uiState.timetableListUiStates[selectedDay]),
                        scrollState = scrollStates.getValue(selectedDay),
                        onTimetableItemClick = onTimetableItemClick,
                        onBookmarkClick = onFavoriteClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            bottom = contentPadding.calculateBottomPadding(),
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                        ),
                    )
                }

                is GridTimetable -> {
                    val timetableStates = rememberGridTimetableStates()
                    TimetableGrid(
                        uiState = requireNotNull(uiState.timetableGridUiState[selectedDay]),
                        timetableState = timetableStates.getValue(selectedDay),
                        timeLine = uiState.timeLine,
                        selectedDay = selectedDay,
                        onTimetableItemClick = onTimetableItemClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            bottom = contentPadding.calculateBottomPadding(),
                            start = contentPadding.calculateStartPadding(layoutDirection),
                        ),
                    )
                }

                Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberListTimetableScrollStates(): Map<DroidKaigi2024Day, LazyListState> {
    val scrollStateMap = DroidKaigi2024Day.visibleDays().associateWith {
        rememberLazyListState()
    }
    return remember { scrollStateMap }
}

@Composable
private fun rememberGridTimetableStates(): Map<DroidKaigi2024Day, TimetableState> {
    val timetableStateMap = DroidKaigi2024Day.visibleDays().associateWith {
        rememberTimetableGridState()
    }
    return remember { timetableStateMap }
}
