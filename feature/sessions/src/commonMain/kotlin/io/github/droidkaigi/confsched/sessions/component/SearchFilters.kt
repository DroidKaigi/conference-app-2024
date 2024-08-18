package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableCategory
import io.github.droidkaigi.confsched.model.TimetableSessionType
import io.github.droidkaigi.confsched.model.fakes
import org.jetbrains.compose.ui.tooling.preview.Preview

const val SearchFiltersFilterDayChipTestTag = "SearchFiltersFilterDayChipTestTag"
const val SearchFiltersFilterCategoryChipTestTag = "SearchFiltersFilterCategoryChipTestTag"

@Composable
fun SearchFilters(
    filterDayUiState: SearchFilterUiState<DroidKaigi2024Day>,
    filterCategoryUiState: SearchFilterUiState<TimetableCategory>,
    filterSessionTypeUiState: SearchFilterUiState<TimetableSessionType>,
    filterLanguageUiState: SearchFilterUiState<Lang>,
    contentPadding: PaddingValues,
    onSelectDay: (DroidKaigi2024Day) -> Unit,
    onSelectCategory: (TimetableCategory) -> Unit,
    onSelectSessionType: (TimetableSessionType) -> Unit,
    onSelectLanguage: (Lang) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = contentPadding,
    ) {
        item {
            FilterDayChip(
                modifier = Modifier.testTag(SearchFiltersFilterDayChipTestTag),
                uiState = filterDayUiState,
                onSelect = onSelectDay,
            )
        }
        item {
            FilterCategoryChip(
                modifier = Modifier.testTag(SearchFiltersFilterCategoryChipTestTag),
                uiState = filterCategoryUiState,
                onSelect = onSelectCategory,
            )
        }
        item {
            FilterSessionTypeChip(
                uiState = filterSessionTypeUiState,
                onSelect = onSelectSessionType,
            )
        }
        item {
            FilterLanguageChip(
                uiState = filterLanguageUiState,
                onSelect = onSelectLanguage,
            )
        }
    }
}

@Composable
@Preview
fun SearchFiltersPreview() {
    KaigiTheme {
        Surface {
            SearchFilters(
                filterDayUiState = SearchFilterUiState(
                    selectableItems = DroidKaigi2024Day.entries,
                    selectedItems = listOf(),
                ),
                filterCategoryUiState = SearchFilterUiState(
                    selectableItems = TimetableCategory.fakes(),
                    selectedItems = listOf(),
                ),
                filterLanguageUiState = SearchFilterUiState(
                    selectableItems = Lang.entries,
                    selectedItems = listOf(),
                ),
                filterSessionTypeUiState = SearchFilterUiState(
                    selectableItems = TimetableSessionType.entries,
                    selectedItems = listOf(),
                ),
                contentPadding = PaddingValues(),
                onSelectDay = {},
                onSelectCategory = {},
                onSelectLanguage = {},
                onSelectSessionType = {},
            )
        }
    }
}
