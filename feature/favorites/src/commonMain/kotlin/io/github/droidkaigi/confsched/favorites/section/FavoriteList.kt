package io.github.droidkaigi.confsched.favorites.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.favorites.section.FavoritesSheetUiState.FavoriteListUiState.TimeSlot
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.component.TimetableItemCard
import io.github.droidkaigi.confsched.ui.component.TimetableItemTag
import io.github.droidkaigi.confsched.ui.component.TimetableTime
import io.github.droidkaigi.confsched.ui.icon
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteList(
    timetableItemMap: PersistentMap<TimeSlot, List<TimetableItem>>,
    onBookmarkClick: (TimetableItem) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val layoutDirection = LocalLayoutDirection.current

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = contentPadding.calculateBottomPadding(),
            start = 16.dp + contentPadding.calculateStartPadding(layoutDirection),
            end = 16.dp + contentPadding.calculateEndPadding(layoutDirection),
        ),
    ) {
        items(
            items = timetableItemMap.toList(),
            key = { it.first.key },
        ) { (time, timetableItems) ->
            Row {
                TimetableTime(
                    startTime = time.startTimeString,
                    endTime = time.endTimeString,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    timetableItems.onEach { timetableItem ->
                        TimetableItemCard(
                            modifier = Modifier.fillMaxWidth(),
                            isBookmarked = true,
                            tags = {
                                TimetableItemTag(
                                    tagText = timetableItem.room.name.currentLangTitle,
                                    icon = timetableItem.room.icon,
                                    tagColor = LocalRoomTheme.current.primaryColor,
                                    modifier = Modifier.background(LocalRoomTheme.current.containerColor),
                                )
                                timetableItem.language.labels.forEach { label ->
                                    TimetableItemTag(
                                        tagText = label,
                                        tagColor = MaterialTheme.colorScheme.outline,
                                    )
                                }
                                timetableItem.day?.let {
                                    TimetableItemTag(
                                        tagText = "9/${it.dayOfMonth}",
                                        tagColor = MaterialTheme.colorScheme.outline,
                                    )
                                }
                            },
                            timetableItem = timetableItem,
                            onTimetableItemClick = onTimetableItemClick,
                            onBookmarkClick = { item, _ -> onBookmarkClick(item) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun FavoriteListPreview() {
    KaigiTheme {
        Surface {
            FavoriteList(
                timetableItemMap = persistentMapOf(
                    TimeSlot(
                        startTimeString = "10:00",
                        endTimeString = "11:00",
                    ) to listOf(
                        Session.fake(),
                    ),
                ),
                onBookmarkClick = {},
                onTimetableItemClick = {},
            )
        }
    }
}
