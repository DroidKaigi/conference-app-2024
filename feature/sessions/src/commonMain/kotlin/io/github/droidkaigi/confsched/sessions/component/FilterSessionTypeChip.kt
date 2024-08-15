package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import conference_app_2024.feature.sessions.generated.resources.Res
import conference_app_2024.feature.sessions.generated.resources.filter_chip_session_type
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.TimetableSessionType
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilterSessionTypeChip(
    uiState: SearchFilterUiState<TimetableSessionType>,
    onSelect: (TimetableSessionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownSearchFilterChip(
        uiState = uiState,
        filterChipLabelDefaultText = stringResource(Res.string.filter_chip_session_type),
        onSelectDropdownMenuItem = onSelect,
        dropdownMenuItemText = {
            Text(text = it.label.currentLangTitle)
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun FilterSessionChipPreview_NotSelected() {
    KaigiTheme {
        FilterSessionTypeChip(
            uiState = SearchFilterUiState(
                selectedItems = listOf(),
                selectableItems = TimetableSessionType.entries,
                selectedValuesText = "",
            ),
            onSelect = {},
        )
    }
}

@Preview
@Composable
fun FilterSessionChipPreview_Selected() {
    KaigiTheme {
        FilterSessionTypeChip(
            uiState = SearchFilterUiState(
                selectedItems = listOf(TimetableSessionType.NORMAL),
                selectableItems = TimetableSessionType.entries,
                selectedValuesText = TimetableSessionType.NORMAL.label.currentLangTitle,
            ),
            onSelect = {},
        )
    }
}
