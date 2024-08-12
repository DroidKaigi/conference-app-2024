package io.github.droidkaigi.confsched.about.component

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
import conference_app_2024.feature.about.generated.resources.place_link
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.designsystem.component.ClickableLinkText
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutDroidKaigiDetailSummaryCardRow(
    leadingIcon: ImageVector,
    label: String,
    content: String,
    modifier: Modifier = Modifier,
    leadingIconContentDescription: String? = null,
    onLinkClick: (url: String) -> Unit = {},
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
        ClickableLinkText(
            style = MaterialTheme.typography.bodyMedium,
            content = content,
            onLinkClick = onLinkClick,
            regex = stringResource(AboutRes.string.place_link).toRegex(),
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
