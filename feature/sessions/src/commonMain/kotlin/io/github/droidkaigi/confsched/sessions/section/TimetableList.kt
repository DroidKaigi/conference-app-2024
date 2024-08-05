package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.ui.component.TimeTableItemCard
import io.github.droidkaigi.confsched.ui.component.TimeTableItemTag
import io.github.droidkaigi.confsched.ui.icon
import kotlinx.collections.immutable.PersistentMap

const val TimetableListTestTag = "TimetableList"

data class TimetableListUiState(
    val timetableItemMap: PersistentMap<String, List<TimetableItem>>,
    val timetable: Timetable,
)

@Composable
fun TimetableList(
    uiState: TimetableListUiState,
    scrollState: LazyListState,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.testTag(TimetableListTestTag),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = contentPadding,
    ) {
        items(uiState.timetable.timetableItems, key = { it.id.value }) { timetableItem ->
            val isBookmarked = uiState.timetable.bookmarks.contains(timetableItem.id)
            TimeTableItemCard(
                isBookmarked = isBookmarked,
                timetableItem = timetableItem,
                onBookmarkClick = onBookmarkClick,
                tags = {
                    TimeTableItemTag(
                        tagText = timetableItem.room.name.currentLangTitle,
                        icon = timetableItem.room.icon,
                        tagColor = LocalRoomTheme.current.primaryColor,
                        modifier = Modifier.background(LocalRoomTheme.current.containerColor),
                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    timetableItem.language.labels.forEach { label ->
                        TimeTableItemTag(tagText = label, tagColor = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.padding(3.dp))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                },
                onTimetableItemClick = onTimetableItemClick,
            )
        }
    }
}
