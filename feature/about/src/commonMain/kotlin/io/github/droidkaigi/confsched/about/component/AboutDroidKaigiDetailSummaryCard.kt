package io.github.droidkaigi.confsched.about.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.about.generated.resources.date_description
import conference_app_2024.feature.about.generated.resources.date_title
import conference_app_2024.feature.about.generated.resources.place_description
import conference_app_2024.feature.about.generated.resources.place_link
import conference_app_2024.feature.about.generated.resources.place_title
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.designsystem.component.ClickableLinkText
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutDroidKaigiDetailSummaryCard(
    modifier: Modifier = Modifier,
    onViewMapClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                1.dp,
            ),
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            AboutDroidKaigiDetailSummaryCardRow(
                leadingIcon = Outlined.Schedule,
                label = stringResource(AboutRes.string.date_title),
                content = stringResource(AboutRes.string.date_description),
            )
            Row {
                AboutDroidKaigiDetailSummaryCardRow(
                    leadingIcon = Outlined.Place,
                    label = stringResource(AboutRes.string.place_title),
                    content = stringResource(AboutRes.string.place_description),
                )
                Spacer(modifier = Modifier.width(8.dp))
                ClickableLinkText(
                    style = MaterialTheme.typography.bodyMedium,
                    content = stringResource(AboutRes.string.place_link),
                    onLinkClick = { _ -> onViewMapClick() },
                    regex = stringResource(AboutRes.string.place_link).toRegex(),
                )
            }
        }
    }
}

@Preview
@Composable
fun AboutDroidKaigiDetailSummaryCardPreview() {
    KaigiTheme {
        Surface {
            AboutDroidKaigiDetailSummaryCard(
                onViewMapClick = {},
            )
        }
    }
}
