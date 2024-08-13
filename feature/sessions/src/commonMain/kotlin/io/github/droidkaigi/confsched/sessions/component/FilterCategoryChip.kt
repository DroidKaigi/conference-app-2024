package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import conference_app_2024.feature.sessions.generated.resources.Res
import conference_app_2024.feature.sessions.generated.resources.filter_chip_category
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.TimetableCategory
import io.github.droidkaigi.confsched.model.fakes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilterCategoryChip(
    uiState: SearchFilterUiState<TimetableCategory>,
    onSelect: (TimetableCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownSearchFilterChip(
        uiState = uiState,
        filterChipLabelDefaultText = stringResource(Res.string.filter_chip_category),
        onSelectDropdownMenuItem = onSelect,
        dropdownMenuItemText = {
            Text(text = it.title.currentLangTitle)
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun FilterCategoryChipPreview() {
    KaigiTheme {
        FilterCategoryChip(
            uiState = SearchFilterUiState(
                selectedItems = listOf(),
                selectableItems = TimetableCategory.fakes(),
                selectedValuesText = "",
            ),
            onSelect = {},
        )
    }
}
