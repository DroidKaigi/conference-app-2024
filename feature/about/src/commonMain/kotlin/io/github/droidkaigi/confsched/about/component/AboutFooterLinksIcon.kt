package io.github.droidkaigi.confsched.about.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.about.generated.resources.icon_youtube
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutFooterLinksIcon(
    testTag: String,
    painter: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(testTag),
    ) {
        Image(
            painter = painter,
            modifier = Modifier.size(48.dp),
            contentDescription = contentDescription,
        )
    }
}

@Preview
@Composable
fun AboutFooterLinksIconPreview() {
    KaigiTheme {
        Surface {
            AboutFooterLinksIcon(
                testTag = "testTag",
                painter = painterResource(AboutRes.drawable.icon_youtube),
                contentDescription = "YouTube",
                onClick = {},
            )
        }
    }
}
