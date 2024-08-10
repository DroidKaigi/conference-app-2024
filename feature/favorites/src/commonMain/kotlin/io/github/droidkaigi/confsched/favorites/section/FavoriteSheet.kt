package io.github.droidkaigi.confsched.favorites.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.favorites.FavoritesScreenEmptyViewTestTag
import io.github.droidkaigi.confsched.favorites.FavoritesSheetUiState
import io.github.droidkaigi.confsched.favorites.component.FavoriteFilters
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.ui.component.TimetableItemCard
import io.github.droidkaigi.confsched.ui.component.TimetableItemTag
import io.github.droidkaigi.confsched.ui.icon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteSheet(
    uiState: FavoritesSheetUiState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
    onBookmarkClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        FavoriteFilters(
            allFilterSelected = uiState.allFilterSelected,
            day1FilterSelected = uiState.day1FilterSelected,
            day2FilterSelected = uiState.day2FilterSelected,
            onAllFilterChipClick = onAllFilterChipClick,
            onDay1FilterChipClick = onDay1FilterChipClick,
            onDay2FilterChipClick = onDay2FilterChipClick,
        )

        when (uiState) {
            is FavoritesSheetUiState.Empty -> {
                EmptyView()
            }

            is FavoritesSheetUiState.FavoriteListUiState -> {
                FavoriteList(
                    timetable = uiState.timeTable,
                    onBookmarkClick = onBookmarkClick,
                    onTimetableItemClick = onTimetableItemClick,
                )
            }
        }
    }
}

@Composable
private fun EmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.testTag(FavoritesScreenEmptyViewTestTag).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(24.dp),
                )
                .size(84.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.Green,
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "登録されたセッションがありません",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "気になるセッションをお気に入り登録しましょう",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
fun FavoriteSheetPreview() {
    KaigiTheme {
        Surface {
            FavoriteSheet(
                uiState = FavoritesSheetUiState.FavoriteListUiState(
                    allFilterSelected = true,
                    day1FilterSelected = false,
                    day2FilterSelected = false,
                    timeTable = Timetable.fake(),
                ),
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
                onBookmarkClick = {},
                onTimetableItemClick = {},
            )
        }
    }
}

@Composable
@Preview
fun FavoriteSheetNoFavoritesPreview() {
    KaigiTheme {
        Surface {
            FavoriteSheet(
                uiState = FavoritesSheetUiState.Empty(
                    allFilterSelected = true,
                    day1FilterSelected = false,
                    day2FilterSelected = false,
                ),
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
                onBookmarkClick = {},
                onTimetableItemClick = {},
            )
        }
    }
}
