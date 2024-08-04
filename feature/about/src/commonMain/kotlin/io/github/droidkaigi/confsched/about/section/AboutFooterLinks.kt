package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.about.generated.resources.app_version
import conference_app_2024.feature.about.generated.resources.content_description_youtube
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.component.AboutFooterLinksIcon
import io.github.droidkaigi.confsched.about.section.AboutFooterLinksSectionTestTag.LinksMediumItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterLinksSectionTestTag.LinksXItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterLinksSectionTestTag.LinksYouTubeItemTestTag
import io.github.droidkaigi.confsched.about.section.AboutFooterLinksSectionTestTag.Section
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

object AboutFooterLinksSectionTestTag {
    const val Section = "FooterLinksSection"
    const val LinksYouTubeItemTestTag = "AboutFooterLinksYouTubeItem"
    const val LinksXItemTestTag = "AboutFooterLinksXItem"
    const val LinksMediumItemTestTag = "AboutFooterLinksMediumItem"
}

@Composable
fun AboutFooterLinks(
    versionName: String?,
    onYouTubeClick: () -> Unit,
    onXClick: () -> Unit,
    onMediumClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .testTag(Section)
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AboutFooterLinksIcon(
                testTag = LinksYouTubeItemTestTag,
                contentDescription = stringResource(AboutRes.string.content_description_youtube),
                onClick = onYouTubeClick,
            )
            AboutFooterLinksIcon(
                testTag = LinksXItemTestTag,
                contentDescription = "X",
                onClick = onXClick,
            )
            AboutFooterLinksIcon(
                testTag = LinksMediumItemTestTag,
                contentDescription = "Medium",
                onClick = onMediumClick,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(AboutRes.string.app_version),
            style = MaterialTheme.typography.labelLarge,
        )
        if (versionName != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = versionName,
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun AboutFooterLinksPreview() {
    KaigiTheme {
        Surface {
            AboutFooterLinks(
                versionName = "1.2",
                onYouTubeClick = {},
                onXClick = {},
                onMediumClick = {},
            )
        }
    }
}
