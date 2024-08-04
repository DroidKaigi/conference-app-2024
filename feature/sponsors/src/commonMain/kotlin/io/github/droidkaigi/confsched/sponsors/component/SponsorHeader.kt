package io.github.droidkaigi.confsched.sponsors.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sponsors.generated.resources.platinum_sponsor
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sponsors.SponsorsRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SponsorHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.Green, // FIXME: use theme color
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(vertical = 6.dp),
    )
}

@Composable
@Preview
fun SponsorHeaderPreview() {
    KaigiTheme {
        Surface {
            SponsorHeader(stringResource(SponsorsRes.string.platinum_sponsor))
        }
    }
}
