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
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Sponsor
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SponsorItem(
    modifier: Modifier = Modifier,
    sponsor: Sponsor,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    Card(
        modifier = modifier.clickable { onSponsorsItemClick(sponsor.link) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
        ),
    ) {
        Image(
            painter = rememberAsyncImagePainter(sponsor.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp,
                ),
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
