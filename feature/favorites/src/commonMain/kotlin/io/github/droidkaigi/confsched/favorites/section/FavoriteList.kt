package io.github.droidkaigi.confsched.favorites.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.component.TimetableItemCard
import io.github.droidkaigi.confsched.ui.component.TimetableItemTag
import io.github.droidkaigi.confsched.ui.icon
import org.jetbrains.compose.ui.tooling.preview.Preview

const val FavoriteLazyColumnTestTag = "FavoriteLazyColumnTestTag"

@Composable
fun FavoriteList(
    timetable: Timetable,
    onBookmarkClick: (TimetableItem) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
            .testTag(FavoriteLazyColumnTestTag),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
    ) {
        items(timetable.timetableItems) { timetableItem ->
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
                    Spacer(modifier = Modifier.padding(3.dp))
                    timetableItem.language.labels.forEach { label ->
                        TimetableItemTag(
                            tagText = label,
                            tagColor = MaterialTheme.colorScheme.outline,
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                    }
                    timetableItem.day?.let {
                        TimetableItemTag(
                            tagText = "9/${it.dayOfMonth}",
                            tagColor = MaterialTheme.colorScheme.outline,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                },
                timetableItem = timetableItem,
                onTimetableItemClick = onTimetableItemClick,
                onBookmarkClick = { item, _ -> onBookmarkClick(item) },
            )
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
