package io.github.droidkaigi.confsched.sponsors.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import conference_app_2024.feature.sponsors.generated.resources.content_description_sponsor_logo_format
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.sponsors.SponsorsRes
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val SponsorItemImageTestTag = "SponsorItemImageTestTag"

@Composable
fun SponsorItem(
    sponsor: Sponsor,
    modifier: Modifier = Modifier,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    Card(
        modifier = modifier.clickable { onSponsorsItemClick(sponsor.link) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
        ),
    ) {
        AsyncImage(
            model = sponsor.logo,
            contentDescription = stringResource(
                SponsorsRes.string.content_description_sponsor_logo_format,
                sponsor.name,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp,
                )
                .testTag(SponsorItemImageTestTag),
        )
    }
}

@Composable
@Preview
fun SponsorItemPreview() {
    KaigiTheme {
        Surface {
            SponsorItem(
                sponsor = Sponsor.fakes().first(),
                onSponsorsItemClick = {},
            )
        }
    }
}
