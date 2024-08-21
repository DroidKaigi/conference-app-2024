package io.github.droidkaigi.confsched.settings.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.settings.generated.resources.section_item_title_font
import conference_app_2024.feature.settings.generated.resources.section_title_accessibility
import io.github.droidkaigi.confsched.model.FontFamily
import io.github.droidkaigi.confsched.settings.SettingsRes
import io.github.droidkaigi.confsched.settings.SettingsUiState
import io.github.droidkaigi.confsched.settings.component.SelectableItemColumn
import io.github.droidkaigi.confsched.settings.component.SettingsItemRow
import org.jetbrains.compose.resources.stringResource

const val SettingsAccessibilityUseFontFamilyTestTag = "SettingsAccessibilityUseFontFamilyTestTag"
const val SettingsAccessibilityUseFontFamilyTestTagPrefix = "SettingsAccessibilityUseFontFamilyTestTag:"

fun LazyListScope.accessibility(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onSelectUseFontFamily: (FontFamily) -> Unit,
) {
    item {
        Text(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                ),
            text = stringResource(SettingsRes.string.section_title_accessibility),
            style = MaterialTheme.typography.titleSmall,
        )
    }
    item {
        SettingsItemRow(
            modifier = modifier.testTag(SettingsAccessibilityUseFontFamilyTestTag),
            leadingIcon = Icons.Default.TextFormat,
            itemName = stringResource(SettingsRes.string.section_item_title_font),
            currentValue = uiState.useFontFamily?.displayName ?: "",
            selectableItems = {
                FontFamily.entries.forEach { fontFamily ->
                    SelectableItemColumn(
                        modifier = Modifier.testTag(
                            SettingsAccessibilityUseFontFamilyTestTagPrefix
                                .plus(fontFamily.displayName),
                        ),
                        currentValue = fontFamily.displayName,
                        onClickItem = {
                            onSelectUseFontFamily(fontFamily)
                        },
                    )
                }
            },
        )
    }

    item {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}
