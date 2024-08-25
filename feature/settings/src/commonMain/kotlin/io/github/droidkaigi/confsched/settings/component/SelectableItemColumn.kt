package io.github.droidkaigi.confsched.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun SelectableItemColumn(
    currentValue: String,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable {
                onClickItem()
            }
            .padding(start = 48.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = currentValue,
            fontFamily = fontFamily,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
