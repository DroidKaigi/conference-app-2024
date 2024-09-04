package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemTag
import io.github.droidkaigi.confsched.model.TimetableItem

@Composable
fun SearchList(
    uiState: TimetableListUiState,
    highlightWord: String,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    TimetableList(
        uiState = uiState,
        scrollState = rememberLazyListState(),
        onBookmarkClick = onBookmarkClick,
        onTimetableItemClick = onTimetableItemClick,
        contentPadding = contentPadding,
        highlightWord = highlightWord,
        modifier = modifier,
        enableAutoScrolling = false,
        timetableItemTagsContent = { timetableItem ->
            timetableItem.day?.monthAndDay()?.let { monthAndDay ->
                TimetableItemTag(tagText = monthAndDay)
            }
        },
    )
}
