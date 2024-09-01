package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sessions.generated.resources.archive
import conference_app_2024.feature.sessions.generated.resources.read_more
import conference_app_2024.feature.sessions.generated.resources.slide
import conference_app_2024.feature.sessions.generated.resources.target_audience
import conference_app_2024.feature.sessions.generated.resources.video
import io.github.droidkaigi.confsched.designsystem.component.ClickableLinkText
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableItem.Special
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val TargetAudienceSectionTestTag = "TargetAudienceSectionTestTag"
const val DescriptionMoreButtonTestTag = "DescriptionMoreButtonTestTag"
const val TimetableItemDetailContentArchiveSectionTestTag = "TimetableItemDetailContentArchiveSectionTestTag"
const val TimetableItemDetailContentArchiveSectionSlideButtonTestTag = "TimetableItemDetailContentArchiveSectionSlideButtonTestTag"
const val TimetableItemDetailContentArchiveSectionVideoButtonTestTag = "TimetableItemDetailContentArchiveSectionVideoButtonTestTag"
const val TimetableItemDetailContentTargetAudienceSectionBottomTestTag = "TimetableItemDetailContentTargetAudienceSectionBottomTestTag"

@Composable
fun TimetableItemDetailContent(
    timetableItem: TimetableItem,
    currentLang: Lang?,
    modifier: Modifier = Modifier,
    onLinkClick: (url: String) -> Unit,
) {
    Column(modifier = modifier) {
        when (timetableItem) {
            is Session -> {
                val currentLang = currentLang ?: Lang.ENGLISH
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
                DescriptionSection(
                    description = timetableItem.description.currentLangTitle,
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
    }
}

@Composable
private fun DescriptionSection(
    description: String,
    onLinkClick: (url: String) -> Unit,
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }
    var isOverFlow by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        SelectionContainer {
            ClickableLinkText(
                content = description,
                regex = "(https)(://[\\w/:%#$&?()~.=+\\-]+)".toRegex(),
                onLinkClick = onLinkClick,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = if (isExpand) Int.MAX_VALUE else 7,
                overflow = if (isExpand) TextOverflow.Clip else TextOverflow.Ellipsis,
                onOverflow = {
                    isOverFlow = it
                },
            )
        }
        Spacer(Modifier.height(16.dp))
        AnimatedVisibility(
            visible = isExpand.not() && isOverFlow,
            enter = EnterTransition.None,
            exit = fadeOut(),
            modifier = Modifier.testTag(DescriptionMoreButtonTestTag),
        ) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { isExpand = true },
            ) {
                Text(
                    text = stringResource(SessionsRes.string.read_more),
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
    Column(
        modifier = modifier
            .padding(8.dp)
            .testTag(TargetAudienceSectionTestTag),
    ) {
        Text(
            text = stringResource(SessionsRes.string.target_audience),
            style = MaterialTheme.typography.titleLarge,
            color = LocalRoomTheme.current.primaryColor,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = timetableItem.targetAudience,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(8.dp).testTag(TimetableItemDetailContentTargetAudienceSectionBottomTestTag))
    }
}

@Composable
private fun ArchiveSection(
    timetableItem: TimetableItem,
    onViewSlideClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    onWatchVideoClick: (url: String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .testTag(TimetableItemDetailContentArchiveSectionTestTag),
    ) {
        Text(
            text = stringResource(SessionsRes.string.archive),
            style = MaterialTheme.typography.titleLarge,
            color = LocalRoomTheme.current.primaryColor,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            timetableItem.asset.slideUrl?.let { slideUrl ->
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag),
                    onClick = { onViewSlideClick(slideUrl) },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = LocalRoomTheme.current.primaryColor,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = stringResource(SessionsRes.string.slide),
                    )
                    Text(
                        text = stringResource(SessionsRes.string.slide),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            timetableItem.asset.videoUrl?.let { videoUrl ->
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag),
                    onClick = { onWatchVideoClick(videoUrl) },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = LocalRoomTheme.current.primaryColor,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayCircle,
                        contentDescription = stringResource(SessionsRes.string.video),
                    )
                    Text(
                        text = stringResource(SessionsRes.string.video),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailContentPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailContent(
                    timetableItem = Session.fake(),
                    currentLang = Lang.JAPANESE,
                    onLinkClick = {},
                )
            }
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailContentWithEnglishPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailContent(
                    timetableItem = Session.fake(),
                    currentLang = Lang.ENGLISH,
                    onLinkClick = {},
                )
            }
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailContentWithMixedPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailContent(
                    timetableItem = Session.fake(),
                    currentLang = Lang.MIXED,
                    onLinkClick = {},
                )
            }
        }
    }
}

@Composable
fun ProvideFakeRoomTheme(content: @Composable () -> Unit) {
    ProvideRoomTheme(Session.fake().room.getThemeKey()) {
        content()
    }
}
