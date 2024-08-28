package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.feature.sessions.generated.resources.category_title
import conference_app_2024.feature.sessions.generated.resources.content_description_category
import conference_app_2024.feature.sessions.generated.resources.content_description_language
import conference_app_2024.feature.sessions.generated.resources.content_description_location
import conference_app_2024.feature.sessions.generated.resources.content_description_schedule
import conference_app_2024.feature.sessions.generated.resources.language_title
import conference_app_2024.feature.sessions.generated.resources.location_title
import conference_app_2024.feature.sessions.generated.resources.schedule_title
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.dashedRoundRect
import io.github.droidkaigi.confsched.model.Locale
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.model.getDefaultLocale
import io.github.droidkaigi.confsched.model.nameAndFloor
import io.github.droidkaigi.confsched.sessions.SessionsRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val SummaryCardTextTag = "SummaryCardTextTag:"

@Composable
fun TimetableItemDetailSummaryCard(
    timetableItem: TimetableItem,
    modifier: Modifier = Modifier,
) {
    val primaryColor = LocalRoomTheme.current.primaryColor
    Column(
        modifier = modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 16.dp,
                bottom = 8.dp,
            )
            .dashedRoundRect(primaryColor)
            .padding(12.dp),
    ) {
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(
                    SummaryCardTextTag.plus(stringResource(SessionsRes.string.schedule_title)),
                ),
            imageVector = Icons.Outlined.Schedule,
            contentDescription = stringResource(SessionsRes.string.content_description_schedule),
            title = stringResource(SessionsRes.string.schedule_title),
            description = timetableItem.formattedDateTimeString,
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(
                    SummaryCardTextTag.plus(stringResource(SessionsRes.string.location_title)),
                ),
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = stringResource(SessionsRes.string.content_description_location),
            title = stringResource(SessionsRes.string.location_title),
            description = timetableItem.room.nameAndFloor,
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(
                    SummaryCardTextTag.plus(stringResource(SessionsRes.string.language_title)),
                ),
            imageVector = Icons.Outlined.Language,
            contentDescription = stringResource(SessionsRes.string.content_description_language),
            title = stringResource(SessionsRes.string.language_title),
            description = timetableItem.getSupportedLangString(
                getDefaultLocale() == Locale.JAPAN,
            ),
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardText(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(
                    SummaryCardTextTag.plus(stringResource(SessionsRes.string.category_title)),
                ),
            imageVector = Icons.Outlined.Category,
            contentDescription = stringResource(SessionsRes.string.content_description_category),
            title = stringResource(SessionsRes.string.category_title),
            description = timetableItem.category.title.currentLangTitle,
        )
    }
}

@Composable
private fun SummaryCardText(
    imageVector: ImageVector,
    contentDescription: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val iconInlineContentId = "icon"
    val spacer8dpInlineContentId = "spacer8dp"
    val spacer12dpInlineContentId = "spacer12dp"

    val annotatedString = createSummaryCardTextAnnotatedString(
        title = title,
        description = description,
        iconInlineContentId = iconInlineContentId,
        spacer8dpInlineContentId = spacer8dpInlineContentId,
        spacer12dpInlineContentId = spacer12dpInlineContentId,
    )

    val inlineContent = createInlineContentsMapForSummaryCardTexts(
        imageVector = imageVector,
        contentDescription = contentDescription,
        iconInlineContentId = iconInlineContentId,
        spacer8dpInlineContentId = spacer8dpInlineContentId,
        spacer12dpInlineContentId = spacer12dpInlineContentId,
    )

    // If multiple Texts are placed, they will not break lines properly when the font scale is large.
    // Therefore, AnnotatedString is used for implementation.
    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.Start),
    )
}

@Composable
private fun createSummaryCardTextAnnotatedString(
    title: String,
    description: String,
    iconInlineContentId: String,
    spacer8dpInlineContentId: String,
    spacer12dpInlineContentId: String,
): AnnotatedString {
    return buildAnnotatedString {
        appendInlineContent(id = iconInlineContentId, alternateText = "[icon]")
        appendInlineContent(id = spacer8dpInlineContentId, alternateText = " ")
        withStyle(
            style = SpanStyle(
                color = LocalRoomTheme.current.primaryColor,
                fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                fontStyle = MaterialTheme.typography.titleSmall.fontStyle,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            ),
        ) {
            append(title)
        }
        appendInlineContent(
            id = spacer12dpInlineContentId,
            alternateText = " ",
        )
        withStyle(
            style = SpanStyle(
                color = LocalContentColor.current,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            ),
        ) {
            append(description)
        }
    }
}

@Composable
private fun createInlineContentsMapForSummaryCardTexts(
    imageVector: ImageVector,
    contentDescription: String,
    iconInlineContentId: String,
    spacer8dpInlineContentId: String,
    spacer12dpInlineContentId: String,
): Map<String, InlineTextContent> {
    return mapOf(
        iconInlineContentId to InlineTextContent(
            placeholder = Placeholder(
                width = MaterialTheme.typography.titleSmall.fontSize,
                height = MaterialTheme.typography.titleSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
            children = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = LocalRoomTheme.current.primaryColor,
                )
            },
        ),
        spacer8dpInlineContentId to InlineTextContent(
            placeholder = Placeholder(
                width = 8.sp,
                height = MaterialTheme.typography.titleSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
            children = {
                Spacer(modifier = Modifier.width(8.dp))
            },
        ),
        spacer12dpInlineContentId to InlineTextContent(
            placeholder = Placeholder(
                width = 12.sp,
                height = MaterialTheme.typography.titleSmall.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
            children = {
                Spacer(modifier = Modifier.width(12.dp))
            },
        ),
    )
}

@Composable
@Preview
fun TimetableItemDetailSummaryCardPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailSummaryCard(
                    timetableItem = TimetableItem.Session.fake(),
                )
            }
        }
    }
}
