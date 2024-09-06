package io.github.droidkaigi.confsched.settings.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.settings.generated.resources.ic_brand_family
import conference_app_2024.feature.settings.generated.resources.section_item_title_font
import conference_app_2024.feature.settings.generated.resources.section_title_accessibility
import io.github.droidkaigi.confsched.designsystem.theme.dotGothic16FontFamily
import io.github.droidkaigi.confsched.model.FontFamily
import io.github.droidkaigi.confsched.model.FontFamily.DotGothic16Regular
import io.github.droidkaigi.confsched.model.FontFamily.SystemDefault
import io.github.droidkaigi.confsched.settings.SettingsRes
import io.github.droidkaigi.confsched.settings.SettingsUiState
import io.github.droidkaigi.confsched.settings.component.SelectableItemColumn
import io.github.droidkaigi.confsched.settings.component.SettingsItemRow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

const val SettingsAccessibilityUseFontFamilyTestTag = "SettingsAccessibilityUseFontFamilyTestTag"
const val SettingsAccessibilityUseFontFamilyTestTagPrefix =
    "SettingsAccessibilityUseFontFamilyTestTag:"

fun LazyListScope.accessibility(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onSelectUseFontFamily: (FontFamily) -> Unit,
) {
    item {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(SettingsRes.string.section_title_accessibility),
            style = MaterialTheme.typography.titleMedium,
        )
    }
    item {
        SettingsItemRow(
            modifier = modifier.testTag(SettingsAccessibilityUseFontFamilyTestTag),
            leadingIcon = vectorResource(SettingsRes.drawable.ic_brand_family),
            itemName = stringResource(SettingsRes.string.section_item_title_font),
            currentValue = uiState.useFontFamily?.displayName ?: "",
            selectableItems = {
                FontFamily.entries.forEach { fontFamily ->
                    val itemFont = when (fontFamily) {
                        DotGothic16Regular -> dotGothic16FontFamily()
                        SystemDefault -> null
                    }
                    SelectableItemColumn(
                        modifier = Modifier.testTag(
                            SettingsAccessibilityUseFontFamilyTestTagPrefix
                                .plus(fontFamily.displayName),
                        ),
                        fontFamily = itemFont,
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
