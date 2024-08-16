package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import conference_app_2024.feature.sessions.generated.resources.Res
import conference_app_2024.feature.sessions.generated.resources.filter_chip_supported_language
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Lang
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilterLanguageChip(
    uiState: SearchFilterUiState<Lang>,
    onSelect: (Lang) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownSearchFilterChip(
        uiState = uiState,
        onSelectDropdownMenuItem = onSelect,
        filterChipLabelDefaultText = stringResource(Res.string.filter_chip_supported_language),
        dropdownMenuItemText = {
            Text(text = it.tagName)
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun FilterLanguageChipPreview_NotSelected() {
    KaigiTheme {
        FilterLanguageChip(
            uiState = SearchFilterUiState(
                selectedItems = listOf(),
                selectableItems = Lang.entries,
                selectedValuesText = "",
            ),
            onSelect = {},
        )
    }
}

@Preview
@Composable
fun FilterLanguageChipPreview_Selected() {
    KaigiTheme {
        FilterLanguageChip(
            uiState = SearchFilterUiState(
                selectedItems = listOf(Lang.JAPANESE),
                selectableItems = Lang.entries,
                selectedValuesText = Lang.JAPANESE.tagName,
            ),
            onSelect = {},
        )
    }
}
