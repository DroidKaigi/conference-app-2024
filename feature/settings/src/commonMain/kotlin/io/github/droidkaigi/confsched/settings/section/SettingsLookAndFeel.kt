package io.github.droidkaigi.confsched.settings.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.settings.generated.resources.disable
import conference_app_2024.feature.settings.generated.resources.enable
import conference_app_2024.feature.settings.generated.resources.section_item_title_enable_animation
import conference_app_2024.feature.settings.generated.resources.section_item_title_enable_fall_back
import conference_app_2024.feature.settings.generated.resources.section_title_title_look_and_feel
import io.github.droidkaigi.confsched.settings.SettingsRes
import io.github.droidkaigi.confsched.settings.SettingsUiState
import io.github.droidkaigi.confsched.settings.component.SettingsItemRow
import org.jetbrains.compose.resources.stringResource

fun LazyListScope.lookAndFeel(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onSelectEnableAnimation: (Boolean) -> Unit,
    onSelectFallbackMode: (Boolean) -> Unit,
) {
    item {
        Text(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                ),
            text = stringResource(SettingsRes.string.section_title_title_look_and_feel),
            style = MaterialTheme.typography.titleSmall,
        )
    }
    item {
        SettingsItemRow(
            modifier = modifier,
            leadingIcon = Icons.Default.Animation,
            itemName = stringResource(SettingsRes.string.section_item_title_enable_animation),
            currentValue = if (uiState.enableAnimation) {
                stringResource(SettingsRes.string.enable)
            } else {
                stringResource(SettingsRes.string.disable)
            },
            onClickItem = {
                onSelectEnableAnimation(uiState.enableAnimation.not())
            },
        )
        SettingsItemRow(
            modifier = modifier,
            leadingIcon = Icons.Default.Memory,
            itemName = stringResource(SettingsRes.string.section_item_title_enable_fall_back),
            currentValue = if (uiState.enableFallbackMode) {
                stringResource(SettingsRes.string.enable)
            } else {
                stringResource(SettingsRes.string.disable)
            },
            onClickItem = {
                onSelectFallbackMode(uiState.enableFallbackMode.not())
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
