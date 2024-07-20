package io.github.droidkaigi.confshed.about.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutDroidKaigiDetailSummaryCardRow(
    leadingIcon: ImageVector,
    label: String,
    content: String,
    modifier: Modifier = Modifier,
    leadingIconContentDescription: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = leadingIconContentDescription,
            modifier = Modifier.size(16.dp),
            tint = Color.Green,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = content,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
fun AboutDroidKaigiDetailSummaryCardRowPreview() {
    KaigiTheme {
        Surface {
            AboutDroidKaigiDetailSummaryCardRow(
                leadingIcon = Filled.Schedule,
                label = "label".repeat(5),
                content = "content".repeat(5),
            )
        }
    }
}
