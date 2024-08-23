package io.github.droidkaigi.confsched.contributors.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.previewOverride
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.Contributor

const val ContributorsItemImageTestTagPrefix = "ContributorsItemImageTestTag:"
const val ContributorsUserNameTextTestTagPrefix = "ContributorsUserNameTextTestTag:"

private val contributorIconShape = CircleShape

@Composable
fun ContributorsItem(
    contributor: Contributor,
    onClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(enabled = !contributor.profileUrl.isNullOrEmpty()) {
                contributor.profileUrl?.let(onClick)
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(23.dp),
    ) {
        Image(
            painter = previewOverride(previewPainter = { rememberVectorPainter(image = Icons.Default.Person) }) {
                rememberAsyncImagePainter(contributor.iconUrl)
            },
            contentDescription = contributor.username,
            modifier = Modifier
                .size(52.dp)
                .clip(contributorIconShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = contributorIconShape,
                )
                .testTag(ContributorsItemImageTestTagPrefix.plus(contributor.username)),
        )
        Text(
            text = contributor.username,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag(ContributorsUserNameTextTestTagPrefix.plus(contributor.username)),
        )
    }
}
