package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.sessions.TimetableListItemBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.TimetableListItemTestTag
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
    val density = LocalDensity.current
    LazyColumn(
        modifier = modifier.testTag(TimetableListTestTag),
        state = scrollState,
        contentPadding = contentPadding,
    ) {
        items(uiState.timetable.timetableItems, key = { it.id.value }) { timetableItem ->
            val isBookmarked = uiState.timetable.bookmarks.contains(timetableItem.id)
            Row {
                Text(
                    text = timetableItem.title.currentLangTitle,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .testTag(TimetableListItemTestTag)
                        .clickable { onTimetableItemClick(timetableItem) },
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.testTag(TimetableListItemBookmarkIconTestTag),
                    onClick = { onBookmarkClick(timetableItem, true) }) {
                    Text(
                        text = "Bookmark ${isBookmarked}",
                    )
                }
            }
        }
    }
}
