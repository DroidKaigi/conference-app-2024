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
import io.github.droidkaigi.confsched.model.TimetableSessionType
import io.github.droidkaigi.confsched.sessions.SessionsStrings.SessionType
import io.github.droidkaigi.confsched.sessions.section.SearchFilterUiState
import kotlinx.collections.immutable.toImmutableList

const val FilterSessionTypeChipTestTag = "FilterSessionTypeChip"

@Composable
fun FilterSessionTypeChip(
    searchFilterUiState: SearchFilterUiState<TimetableSessionType>,
    onSessionTypeSelected: (TimetableSessionType, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onFilterSessionTypeChipClicked: () -> Unit,
) {
    DropdownFilterChip(
        searchFilterUiState = searchFilterUiState,
        onSelected = onSessionTypeSelected,
        filterChipLabelDefaultText = SessionType.asString(),
        onFilterChipClick = onFilterSessionTypeChipClicked,
        dropdownMenuItemText = { sessionType ->
            sessionType.label.currentLangTitle
        },
        modifier = modifier.testTag(FilterSessionTypeChipTestTag),
    )
}

@MultiThemePreviews
@Composable
fun PreviewFilterSessionTypeChip() {
    var uiState by remember {
        mutableStateOf(
            SearchFilterUiState(
                selectedItems = emptyList<TimetableSessionType>().toImmutableList(),
                items = TimetableSessionType.entries.toImmutableList(),
            ),
        )
    }

    KaigiTheme {
        FilterSessionTypeChip(
            searchFilterUiState = uiState,
            onSessionTypeSelected = { sessionType, isSelected ->
                val selectedSessionTypes = uiState.selectedItems.toMutableList()
                val newSelectedSessionTypes = selectedSessionTypes.apply {
                    if (isSelected) {
                        add(sessionType)
                    } else {
                        remove(sessionType)
                    }
                }
                uiState = uiState.copy(
                    selectedItems = newSelectedSessionTypes.toImmutableList(),
                    isSelected = newSelectedSessionTypes.isNotEmpty(),
                    selectedValues = newSelectedSessionTypes.joinToString { it.label.currentLangTitle },
                )
            },
            onFilterSessionTypeChipClicked = {},
        )
    }
}
