package io.github.droidkaigi.confsched.favorites.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoriteFilters(
    allFilterSelected: Boolean,
    day1FilterSelected: Boolean,
    day2FilterSelected: Boolean,
    onAllFilterChipClick: () -> Unit,
    onDay1FilterChipClick: () -> Unit,
    onDay2FilterChipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = allFilterSelected,
            onClick = onAllFilterChipClick,
            label = { Text("すべて") },
            leadingIcon = {
                if (allFilterSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                    )
                }
            },
        )
        FilterChip(
            selected = day1FilterSelected,
            onClick = onDay1FilterChipClick,
            label = { Text("9/12") },
            leadingIcon = {
                if (day1FilterSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                    )
                }
            },
        )
        FilterChip(
            selected = day2FilterSelected,
            onClick = onDay2FilterChipClick,
            label = { Text("9/13") },
            leadingIcon = {
                if (day2FilterSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                    )
                }
            },
        )
    }
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
                onAllFilterChipClick = {},
                onDay1FilterChipClick = {},
                onDay2FilterChipClick = {},
            )
        }
    }
}
