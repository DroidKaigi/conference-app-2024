package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailBookmarkIconTestTag

@Composable
fun TimetableItemDetailBottomAppBar(
    timetableItem: TimetableItem,
    isBookmarked: Boolean,
    onBookmarkClick: (TimetableItem) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = { onShareClick(timetableItem) }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                )
            }
            IconButton(onClick = { onCalendarRegistrationClick(timetableItem) }) {
                Icon(
                    imageVector = Icons.Outlined.EditCalendar,
                    contentDescription = "Calendar",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag(TimetableItemDetailBookmarkIconTestTag),
                onClick = { onBookmarkClick(timetableItem) },
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Bookmarked",
                )
            }
        },
    )
}
