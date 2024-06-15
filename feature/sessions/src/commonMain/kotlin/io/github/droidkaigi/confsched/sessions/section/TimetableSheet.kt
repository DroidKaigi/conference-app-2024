package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import io.github.droidkaigi.confsched.model.DroidKaigi2023Day
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.sessions.section.TimetableSheetUiState.Empty
import io.github.droidkaigi.confsched.sessions.section.TimetableSheetUiState.GridTimetable
import io.github.droidkaigi.confsched.sessions.section.TimetableSheetUiState.ListTimetable
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalClock

const val TimetableTabTestTag = "TimetableTab"

sealed interface TimetableSheetUiState {
    data object Empty : TimetableSheetUiState
    data class ListTimetable(
        val timetableListUiStates: Map<DroidKaigi2023Day, TimetableListUiState>,
    ) : TimetableSheetUiState

    data class GridTimetable(
        val timetableGridUiState: Map<DroidKaigi2023Day, TimetableGridUiState>,
    ) : TimetableSheetUiState
}

@Composable
fun TimetableSheet(
    uiState: TimetableSheetUiState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onFavoriteClick: (TimetableItem, Boolean) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val clock = LocalClock.current
    var selectedDay by rememberSaveable { mutableStateOf(DroidKaigi2023Day.initialSelectedDay(clock)) }
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = modifier.padding(contentPadding.calculateTopPadding()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            when (uiState) {
                is ListTimetable -> {
                    TimetableList(
                        uiState = requireNotNull(uiState.timetableListUiStates[selectedDay]),
                        scrollState = rememberLazyListState(),
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
                    TimetableGrid(
                        uiState = requireNotNull(uiState.timetableGridUiState[selectedDay]),
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
                    Text("Empty")
                }
            }
        }
    }
}
