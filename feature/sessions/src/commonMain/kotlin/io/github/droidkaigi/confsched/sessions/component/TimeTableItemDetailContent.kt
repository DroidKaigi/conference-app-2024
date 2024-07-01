package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.component.ClickableLinkText
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableItem.Special

@Composable
fun TimeTableItemDetailContent(
    timetableItem: TimetableItem,
    modifier: Modifier = Modifier,
    onLinkClick: (url: String) -> Unit,
) {
    Column(modifier = modifier) {
        DescriptionSection(
            timetableItem = timetableItem,
            onLinkClick = onLinkClick,
        )
        TargetAudienceSection(timetableItem = timetableItem)
        if (timetableItem.asset.isAvailable) {
            ArchiveSection(
                timetableItem = timetableItem,
                onViewSlideClick = onLinkClick,
                onWatchVideoClick = onLinkClick,
            )
        }
    }
}

@Composable
private fun DescriptionSection(
    timetableItem: TimetableItem,
    onLinkClick: (url: String) -> Unit,
) {
    var isExpand by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        ClickableLinkText(
            content = when (timetableItem) {
                is Session -> timetableItem.description
                is Special -> timetableItem.description
            }.currentLangTitle,
            regex = "(https)(://[\\w/:%#$&?()~.=+\\-]+)".toRegex(),
            onLinkClick = onLinkClick,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (isExpand) Int.MAX_VALUE else 7,
            overflow = if (isExpand) TextOverflow.Clip else TextOverflow.Ellipsis,
        )
        if (isExpand.not()) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                onClick = { isExpand = true },
            ) {
                Text(
                    text = "続きを読む",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun TargetAudienceSection(
    timetableItem: TimetableItem,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "対象者",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = timetableItem.targetAudience,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun ArchiveSection(
    timetableItem: TimetableItem,
    onViewSlideClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    onWatchVideoClick: (url: String) -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "アーカイブ",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            timetableItem.asset.slideUrl?.let { slideUrl ->
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewSlideClick(slideUrl) },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = "Slide",
                    )
                    Text(
                        text = "スライド",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            timetableItem.asset.videoUrl?.let { videoUrl ->
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onWatchVideoClick(videoUrl) },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayCircle,
                        contentDescription = "Video",
                    )
                    Text(
                        text = "動画",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
