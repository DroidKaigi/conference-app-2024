package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sessions.generated.resources.english
import conference_app_2024.feature.sessions.generated.resources.japanese
import conference_app_2024.feature.sessions.generated.resources.select_language
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemTag
import io.github.droidkaigi.confsched.droidkaigiui.icon
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val TimetableItemDetailHeadlineTestTag = "TimetableItemDetailHeadlineTestTag"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimetableItemDetailHeadline(
    currentLang: Lang?,
    timetableItem: TimetableItem,
    isLangSelectable: Boolean,
    onLanguageSelect: (Lang) -> Unit,
    modifier: Modifier = Modifier,
) {
    val roomTheme = LocalRoomTheme.current
    val currentLang = currentLang ?: timetableItem.language.toLang()

    Column(
        modifier = Modifier
            .background(roomTheme.dimColor)
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .then(modifier),
    ) {
        FlowRow {
            TimetableItemTag(
                modifier = Modifier.align(Alignment.CenterVertically),
                tagText = timetableItem.room.name.currentLangTitle,
                tagColor = roomTheme.primaryColor,
                icon = timetableItem.room.icon,
            )
            timetableItem.language.labels.forEach { label ->
                Spacer(modifier = Modifier.padding(4.dp))
                TimetableItemTag(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    tagText = label,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (isLangSelectable) {
                LanguageSwitcher(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    currentLang = currentLang,
                    onLanguageSelect = onLanguageSelect,
                )
            }
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
private fun LanguageSwitcher(
    currentLang: Lang,
    onLanguageSelect: (Lang) -> Unit,
    modifier: Modifier = Modifier,
) {
    val normalizedCurrentLang = if (currentLang == Lang.MIXED) {
        Lang.ENGLISH
    } else {
        currentLang
    }
    val availableLangs: Map<String, Lang> = mapOf(
        stringResource(SessionsRes.string.japanese) to Lang.JAPANESE,
        stringResource(SessionsRes.string.english) to Lang.ENGLISH,
    )
    val switcherContentDescription = stringResource(SessionsRes.string.select_language)
    Row(
        modifier = modifier
            .selectableGroup()
            .semantics {
                contentDescription = switcherContentDescription
                collectionInfo = CollectionInfo(
                    rowCount = availableLangs.size,
                    columnCount = 1,
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val lastIndex = availableLangs.size - 1
        availableLangs.entries.forEachIndexed { index, (label, lang) ->
            val isSelected = normalizedCurrentLang == lang
            TextButton(
                onClick = { onLanguageSelect(lang) },
                modifier = Modifier
                    .semantics {
                        role = Role.Tab
                        selected = isSelected
                        collectionItemInfo = CollectionItemInfo(
                            rowIndex = index,
                            rowSpan = 1,
                            columnIndex = 0,
                            columnSpan = 1,
                        )
                    },
                contentPadding = PaddingValues(12.dp),
            ) {
                val contentColor = if (isSelected) {
                    LocalRoomTheme.current.primaryColor
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
                AnimatedVisibility(isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(12.dp),
                        tint = contentColor,
                    )
                }
                Text(
                    text = label,
                    color = contentColor,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            if (index < lastIndex) {
                VerticalDivider(
                    modifier = Modifier.height(11.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
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
                    isLangSelectable = true,
                    onLanguageSelect = {},
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
                    isLangSelectable = true,
                    onLanguageSelect = {},
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
                    isLangSelectable = true,
                    onLanguageSelect = {},
                )
            }
        }
    }
}
