package io.github.droidkaigi.confshed.about.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AboutFooterLinksIcon(
    testTag: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .testTag(testTag)
            .size(48.dp),
    ) {
        // TODO
        Icon(
            Icons.Filled.Favorite,
            contentDescription = contentDescription,
            tint = Color.Green,
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
                contentDescription = "YouTube",
                onClick = {},
            )
        }
    }
}
