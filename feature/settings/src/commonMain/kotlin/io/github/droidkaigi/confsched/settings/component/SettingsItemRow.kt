package io.github.droidkaigi.confsched.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItemRow(
    leadingIcon: ImageVector,
    itemName: String,
    currentValue: String,
    modifier: Modifier = Modifier,
    selectableItems: (@Composable () -> Unit)? = null,
    onClickItem: (() -> Unit)? = null,
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClickItem?.invoke()
                isExpand = isExpand.not()
            },
    ) {
        Row(
            modifier = Modifier
                .height(72.dp)
                .align(
                    alignment = Alignment.Start,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                    ),
            )
            Column {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = currentValue,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        if (isExpand) {
            selectableItems?.invoke()
        }
    }
}
