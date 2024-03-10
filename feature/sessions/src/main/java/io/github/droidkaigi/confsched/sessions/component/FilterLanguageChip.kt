package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import io.github.droidkaigi.confsched.designsystem.preview.MultiThemePreviews
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.Lang.ENGLISH
import io.github.droidkaigi.confsched.model.Lang.JAPANESE
import io.github.droidkaigi.confsched.sessions.SessionsStrings.SupportedLanguages
import io.github.droidkaigi.confsched.sessions.section.SearchFilterUiState
import kotlinx.collections.immutable.toImmutableList

const val FilterLanguageChipTestTag = "FilterLanguageChip"

@Composable
fun FilterLanguageChip(
    searchFilterUiState: SearchFilterUiState<Lang>,
    onLanguagesSelected: (Lang, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onFilterLanguageChipClicked: () -> Unit,
) {
    DropdownFilterChip(
        searchFilterUiState = searchFilterUiState,
        onSelected = onLanguagesSelected,
        filterChipLabelDefaultText = SupportedLanguages.asString(),
        onFilterChipClick = onFilterLanguageChipClicked,
        dropdownMenuItemText = { language ->
            language.tagName
        },
        modifier = modifier.testTag(FilterLanguageChipTestTag),
    )
}

@MultiThemePreviews
@Composable
fun PreviewFilterLanguageChip() {
    var uiState by remember {
        mutableStateOf(
            SearchFilterUiState(
                selectedItems = emptyList<Lang>().toImmutableList(),
                items = listOf(JAPANESE, ENGLISH).toImmutableList(),
            ),
        )
    }

    KaigiTheme {
        FilterLanguageChip(
            searchFilterUiState = uiState,
            onLanguagesSelected = { language, isSelected ->
                val selectedLanguages = uiState.selectedItems.toMutableList()
                val newSelectedLanguages = selectedLanguages.apply {
                    if (isSelected) {
                        add(language)
                    } else {
                        remove(language)
                    }
                }
                uiState = uiState.copy(
                    selectedItems = newSelectedLanguages.toImmutableList(),
                    isSelected = newSelectedLanguages.isNotEmpty(),
                    selectedValues = newSelectedLanguages.joinToString { it.tagName },
                )
            },
            onFilterLanguageChipClicked = {},
        )
    }
}
