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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.MultiLangText
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableItem.Special
import io.github.droidkaigi.confsched.model.fake
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimeTableItemDetailContent(
    timetableItem: TimetableItem,
    currentLang: Lang?,
    modifier: Modifier = Modifier,
    onLinkClick: (url: String) -> Unit,
) {
    Column(modifier = modifier) {
        when (timetableItem) {
            is Session -> {
                val currentLang = currentLang ?: Lang.ENGLISH
                fun MultiLangText.getByLang(lang: Lang): String {
                    return if (lang == Lang.JAPANESE) {
                        jaTitle
                    } else {
                        enTitle
                    }
                }
                DescriptionSection(
                    description = timetableItem.description.getByLang(currentLang),
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

            is Special -> {
                Text("Special")
            }
        }
    }
}

@Composable
private fun DescriptionSection(
    description: String,
    onLinkClick: (url: String) -> Unit,
) {
    var isExpand by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        ClickableLinkText(
            content = description,
            regex = "(https)(://[\\w/:%#$&?()~.=+\\-]+)".toRegex(),
            onLinkClick = onLinkClick,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (isExpand) Int.MAX_VALUE else 7,
            overflow = if (isExpand) TextOverflow.Clip else TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(16.dp))
        if (isExpand.not()) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { isExpand = true },
            ) {
                Text(
                    text = "続きを読む",
                    style = MaterialTheme.typography.labelLarge,
                    color = LocalRoomTheme.current.primaryColor,
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TargetAudienceSection(
    timetableItem: TimetableItem,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            text = "対象者",
            style = MaterialTheme.typography.titleLarge,
            color = LocalRoomTheme.current.primaryColor,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = timetableItem.targetAudience,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ArchiveSection(
    timetableItem: TimetableItem,
    onViewSlideClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    onWatchVideoClick: (url: String) -> Unit,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            text = "アーカイブ",
            style = MaterialTheme.typography.titleLarge,
            color = LocalRoomTheme.current.primaryColor,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            timetableItem.asset.slideUrl?.let { slideUrl ->
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewSlideClick(slideUrl) },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = LocalRoomTheme.current.primaryColor,
                    ),
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
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = LocalRoomTheme.current.primaryColor,
                    ),
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

@Composable
@Preview
fun TimeTableItemDetailContentPreview() {
    KaigiTheme {
        Surface {
            TimeTableItemDetailContent(
                timetableItem = Session.fake(),
                currentLang = Lang.JAPANESE,
                onLinkClick = {},
            )
        }
    }
}

@Composable
@Preview
fun TimeTableItemDetailContentWithEnglishPreview() {
    KaigiTheme {
        Surface {
            TimeTableItemDetailContent(
                timetableItem = Session.fake(),
                currentLang = Lang.ENGLISH,
                onLinkClick = {},
            )
        }
    }
}

@Composable
@Preview
fun TimeTableItemDetailContentWithMixedPreview() {
    KaigiTheme {
        Surface {
            TimeTableItemDetailContent(
                timetableItem = Session.fake(),
                currentLang = Lang.MIXED,
                onLinkClick = {},
            )
        }
    }
}
