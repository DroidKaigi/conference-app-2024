package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import conference_app_2024.feature.sessions.generated.resources.Res
import conference_app_2024.feature.sessions.generated.resources.filter_chip_day
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilterDayChip(
    uiState: SearchFilterUiState<DroidKaigi2024Day>,
    onSelect: (DroidKaigi2024Day) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownSearchFilterChip(
        uiState = uiState,
        onSelectDropdownMenuItem = onSelect,
        filterChipLabelDefaultText = stringResource(Res.string.filter_chip_day),
        dropdownMenuItemText = {
            Text(it.monthAndDay())
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun FilterDayChipPreview_NotSelected() {
    KaigiTheme {
        FilterDayChip(
            uiState = SearchFilterUiState(
                selectedItems = listOf(),
                selectableItems = listOf(
                    DroidKaigi2024Day.ConferenceDay1,
                    DroidKaigi2024Day.ConferenceDay2,
                ),
                selectedValuesText = "",
            ),
            onSelect = {},
        )
    }
}

@Preview
@Composable
fun FilterDayChipPreview_Selected() {
    KaigiTheme {
        FilterDayChip(
            uiState = SearchFilterUiState(
                selectedItems = listOf(DroidKaigi2024Day.ConferenceDay1),
                selectableItems = listOf(
                    DroidKaigi2024Day.ConferenceDay1,
                    DroidKaigi2024Day.ConferenceDay2,
                ),
                selectedValuesText = "9/12",
            ),
            onSelect = {},
        )
    }
}
