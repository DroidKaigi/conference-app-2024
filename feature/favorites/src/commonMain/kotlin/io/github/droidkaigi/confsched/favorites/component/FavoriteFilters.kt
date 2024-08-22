package io.github.droidkaigi.confsched.favorites.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.favorites.generated.resources.filter_all
import conference_app_2024.feature.favorites.generated.resources.filter_day1
import conference_app_2024.feature.favorites.generated.resources.filter_day2
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.favorites.FavoritesRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteFilters(
    allFilterSelected: Boolean,
    day1FilterSelected: Boolean,
    day2FilterSelected: Boolean,
    backgroundColor: Color,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        FavoriteFilterChip(
            selected = allFilterSelected,
            onClick = onAllFilterChipClick,
            text = stringResource(FavoritesRes.string.filter_all),
        )
        FavoriteFilterChip(
            selected = day1FilterSelected,
            onClick = onDay1FilterChipClick,
            text = stringResource(FavoritesRes.string.filter_day1),
        )
        FavoriteFilterChip(
            selected = day2FilterSelected,
            onClick = onDay2FilterChipClick,
            text = stringResource(FavoritesRes.string.filter_day2),
        )
    }
}

@Composable
private fun FavoriteFilterChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        modifier = modifier.padding(top = 8.dp, bottom = 12.dp),
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        leadingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                )
            }
        },
    )
}

@Composable
@Preview
fun FavoriteFiltersPreview() {
    KaigiTheme {
        Surface {
            FavoriteFilters(
                allFilterSelected = false,
                day1FilterSelected = true,
                day2FilterSelected = true,
                backgroundColor = MaterialTheme.colorScheme.surface,
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
            )
        }
    }
}
