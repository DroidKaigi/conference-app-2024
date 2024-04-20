package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem

data class TimetableGridUiState(val timetable: Timetable)

@Composable
fun TimetableGrid(
    uiState: TimetableGridUiState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    TimetableGrid(
        timetable = uiState.timetable,
        onTimetableItemClick = onTimetableItemClick,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

@Composable
fun TimetableGrid(
    timetable: Timetable,
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(modifier.padding(contentPadding)) {
        timetable.timetableItems.forEach {
            Text(
                text = it.title.currentLangTitle,
                modifier = Modifier.padding(16.dp)
                    .clickable { onTimetableItemClick(it) },
            )
        }
    }
}
