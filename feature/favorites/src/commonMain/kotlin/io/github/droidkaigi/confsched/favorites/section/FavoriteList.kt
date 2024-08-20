package io.github.droidkaigi.confsched.favorites.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.component.TimetableItemCard
import io.github.droidkaigi.confsched.ui.component.TimetableItemTag
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalIsWideWidthScreen
import io.github.droidkaigi.confsched.ui.icon
import io.github.droidkaigi.confsched.ui.plus
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteList(
    timetable: Timetable,
    onBookmarkClick: (TimetableItem) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val isWideWidthScreen = LocalIsWideWidthScreen.current
    val columnNum by remember { derivedStateOf { if (isWideWidthScreen) 2 else 1 } }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = contentPadding + PaddingValues(horizontal = 16.dp),
    ) {
        items(timetable.timetableItems.windowed(columnNum, columnNum, true)) { windowedItems ->
            Row(
                modifier = Modifier.height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                windowedItems.forEach { timetableItem ->
                    TimetableItemCard(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1F),
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
                if (windowedItems.size < columnNum) {
                    Spacer(modifier = Modifier.weight((columnNum - windowedItems.size).toFloat()))
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
                timetable = Timetable.fake(),
                onBookmarkClick = {},
                onTimetableItemClick = {},
            )
        }
    }
}
