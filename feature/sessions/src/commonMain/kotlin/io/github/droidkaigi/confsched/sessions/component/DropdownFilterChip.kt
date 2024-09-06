package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val DropdownFilterChipTestTagPrefix = "DropdownFilterChipTestTag:"

data class SearchFilterUiState<T>(
    val selectedItems: List<T>,
    val selectableItems: List<T>,
    val selectedValuesText: String = "",
) {
    val isSelected: Boolean get() = selectedItems.isNotEmpty()
}

@Composable
fun <T> DropdownFilterChip(
    uiState: SearchFilterUiState<T>,
    filterChipLabel: @Composable () -> Unit,
    filterChipLeadingIcon: @Composable () -> Unit,
    filterChipTrailingIcon: @Composable () -> Unit,
    dropdownMenuText: @Composable (T) -> Unit,
    dropdownMenuItemLeadingIcon: @Composable (T) -> Unit,
    dropdownMenuItemTrailingIcon: @Composable (T) -> Unit,
    onSelectDropdownMenuItem: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        FilterChip(
            selected = uiState.isSelected,
            onClick = {
                expanded = true
            },
            label = filterChipLabel,
            leadingIcon = filterChipLeadingIcon,
            trailingIcon = filterChipTrailingIcon,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            uiState.selectableItems.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.testTag(DropdownFilterChipTestTagPrefix.plus(item)),
                    text = { dropdownMenuText(item) },
                    onClick = {
                        onSelectDropdownMenuItem(item)
                        expanded = false
                    },
                    leadingIcon = { dropdownMenuItemLeadingIcon(item) },
                    trailingIcon = { dropdownMenuItemTrailingIcon(item) },
                )
            }
        }
    }
}

@Composable
fun <T> DropdownSearchFilterChip(
    uiState: SearchFilterUiState<T>,
    filterChipLabelDefaultText: String,
    onSelectDropdownMenuItem: (T) -> Unit,
    dropdownMenuItemText: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownFilterChip(
        uiState = uiState,
        filterChipLabel = {
            Text(
                text = uiState.selectedValuesText.ifEmpty {
                    filterChipLabelDefaultText
                },
            )
        },
        filterChipLeadingIcon = {
            if (uiState.isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }
        },
        filterChipTrailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (uiState.isSelected) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(18.dp),
            )
        },
        dropdownMenuText = dropdownMenuItemText,
        dropdownMenuItemTrailingIcon = { /* no trailing icon */ },
        dropdownMenuItemLeadingIcon = { item ->
            if (uiState.selectedItems.contains(item)) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                )
            }
        },
        onSelectDropdownMenuItem = onSelectDropdownMenuItem,
        modifier = modifier,
    )
}
