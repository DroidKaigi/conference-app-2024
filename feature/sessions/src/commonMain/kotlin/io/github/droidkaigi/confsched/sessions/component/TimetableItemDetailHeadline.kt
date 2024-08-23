package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sessions.generated.resources.english
import conference_app_2024.feature.sessions.generated.resources.japanese
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.ui.component.TimetableItemTag
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val TimetableItemDetailHeadlineTestTag = "TimetableItemDetailHeadlineTestTag"

@Composable
fun TimetableItemDetailHeadline(
    currentLang: Lang?,
    timetableItem: TimetableItem,
    onLanguageSwitch: (Lang) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentLang = currentLang ?: Lang.ENGLISH
    Column(
        modifier = modifier
            // FIXME: Implement and use a theme color instead of fixed colors like RoomColors.primary and RoomColors.primaryDim
            .background(LocalRoomTheme.current.dimColor)
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Row {
            TimetableItemTag(
                tagText = timetableItem.room.name.currentLangTitle,
                tagColor = LocalRoomTheme.current.primaryColor,
            )
            timetableItem.language.labels.forEach { label ->
                Spacer(modifier = Modifier.padding(4.dp))
                TimetableItemTag(
                    tagText = label,
                    tagColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TimetableItemDetailLanguageSwitcher(
                currentLang = currentLang,
                onLanguageSwitch = onLanguageSwitch,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.testTag(TimetableItemDetailHeadlineTestTag),
            text = timetableItem.title.getByLang(currentLang),
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
        timetableItem.speakers.forEach { speaker ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(speaker.iconUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .border(border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant), shape = CircleShape)
                        .clip(CircleShape)
                        .size(52.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = speaker.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = speaker.tagLine,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TimetableItemDetailLanguageSwitcher(
    currentLang: Lang,
    onLanguageSwitch: (Lang) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        TextButton(onClick = { onLanguageSwitch(Lang.JAPANESE) }) {
            Text(
                text = stringResource(SessionsRes.string.japanese),
                color = if (currentLang == Lang.JAPANESE) {
                    LocalRoomTheme.current.primaryColor
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.labelMedium,
            )
        }
        VerticalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        TextButton(onClick = { onLanguageSwitch(Lang.ENGLISH) }) {
            Text(
                text = stringResource(SessionsRes.string.english),
                color = if (currentLang == Lang.ENGLISH) {
                    LocalRoomTheme.current.primaryColor
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailHeadlinePreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailHeadline(
                    timetableItem = TimetableItem.Session.fake(),
                    currentLang = Lang.JAPANESE,
                    onLanguageSwitch = {},
                )
            }
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailHeadlineWithEnglishPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailHeadline(
                    timetableItem = TimetableItem.Session.fake(),
                    currentLang = Lang.ENGLISH,
                    onLanguageSwitch = {},
                )
            }
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailHeadlineWithMixedPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailHeadline(
                    timetableItem = TimetableItem.Session.fake(),
                    currentLang = Lang.MIXED,
                    onLanguageSwitch = {},
                )
            }
        }
    }
}
