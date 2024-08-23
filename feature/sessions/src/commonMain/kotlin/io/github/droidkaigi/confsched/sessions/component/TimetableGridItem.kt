package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.feature.sessions.generated.resources.content_description_error_icon
import conference_app_2024.feature.sessions.generated.resources.content_description_user_icon
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.icon
import io.github.droidkaigi.confsched.droidkaigiui.previewOverride
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.MultiLangText
import io.github.droidkaigi.confsched.model.RoomType.RoomH
import io.github.droidkaigi.confsched.model.TimetableAsset
import io.github.droidkaigi.confsched.model.TimetableCategory
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableItemId
import io.github.droidkaigi.confsched.model.TimetableLanguage
import io.github.droidkaigi.confsched.model.TimetableRoom
import io.github.droidkaigi.confsched.model.TimetableSessionType
import io.github.droidkaigi.confsched.model.TimetableSpeaker
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.section.TimetableSizes
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ceil
import kotlin.math.roundToInt

const val TimetableGridItemTestTag = "TimetableGridItem"

@Composable
fun TimetableGridItem(
    timetableItem: TimetableItem,
    onTimetableItemClick: (TimetableItem) -> Unit,
    gridItemHeightPx: Int,
    modifier: Modifier = Modifier,
) {
    val localDensity = LocalDensity.current
    val localFontScale = LocalDensity.current.fontScale

    val speaker = timetableItem.speakers.firstOrNull()
    val speakers = timetableItem.speakers

    val height = with(localDensity) { gridItemHeightPx.toDp() }
    val titleMinHeightDp =
        (TimetableGridItemSizes.minTitleLineHeight.value * localFontScale / localDensity.density).dp

    // Narrow the padding after pinching in to a certain size
    val isNarrowedVerticalPadding by remember(height) {
        // `4` is the number of weights set in display area
        derivedStateOf { (height - TimetableGridItemSizes.padding * 2) / 4 < titleMinHeightDp * 3 }
    }

    // Exclude all except title because pinching in to a certain size makes it difficult to see title
    val isShowingAllContent by remember(height) {
        // `4` is the number of weights set in display area
        derivedStateOf { (height - TimetableGridItemSizes.padding * 2) / 4 > titleMinHeightDp * 3 / 2 }
    }

    ProvideRoomTheme(timetableItem.room.getThemeKey()) {
        val titleTextStyle = MaterialTheme.typography.labelLarge.let {
            check(it.fontSize.isSp)
            val (titleFontSize, titleLineHeight) = calculateFontSizeAndLineHeight(
                textStyle = it,
                localDensity = localDensity,
                gridItemHeightPx = gridItemHeightPx,
                speaker = speaker,
                titleLength = timetableItem.title.currentLangTitle.length,
            )
            it.copy(
                fontSize = titleFontSize,
                lineHeight = titleLineHeight,
                color = LocalRoomTheme.current.primaryColor,
            )
        }
        val cardShape = RoundedCornerShape(4.dp)
        Column(
            modifier = modifier
                .testTag(TimetableGridItemTestTag)
                .background(
                    color = LocalRoomTheme.current.containerColor,
                    shape = cardShape,
                )
                .border(
                    width = 1.dp,
                    color = LocalRoomTheme.current.primaryColor,
                    shape = cardShape,
                )
                .width(TimetableGridItemSizes.width)
                .height(height)
                .clickable {
                    onTimetableItemClick(timetableItem)
                }
                .padding(
                    horizontal = TimetableGridItemSizes.padding,
                    vertical = if (isNarrowedVerticalPadding) {
                        TimetableGridItemSizes.padding / 2
                    } else {
                        TimetableGridItemSizes.padding
                    },
                ),
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(
                    space = TimetableGridItemSizes.scheduleToTitleSpace,
                    alignment = if (isShowingAllContent) Alignment.Top else Alignment.CenterVertically,
                ),
            ) {
                if (isShowingAllContent) {
                    Row(
                        modifier = Modifier
                            .weight(1f, fill = false),
                    ) {
                        Icon(
                            modifier = Modifier.height(TimetableGridItemSizes.scheduleHeight),
                            imageVector = vectorResource(checkNotNull(timetableItem.room.icon)),
                            contentDescription = timetableItem.room.name.currentLangTitle,
                            tint = LocalRoomTheme.current.primaryColor,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        var scheduleTextStyle = MaterialTheme.typography.labelSmall
                        if (titleTextStyle.fontSize < scheduleTextStyle.fontSize) {
                            scheduleTextStyle =
                                scheduleTextStyle.copy(fontSize = titleTextStyle.fontSize)
                        }
                        Text(
                            text = "${timetableItem.startsTimeString}~${timetableItem.endsTimeString}",
                            style = scheduleTextStyle,
                            color = LocalRoomTheme.current.primaryColor,
                        )
                    }
                }

                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = timetableItem.title.currentLangTitle,
                    style = titleTextStyle,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            val shouldShowError = timetableItem is Session && timetableItem.message != null

            if (isShowingAllContent && (speakers.isNotEmpty() || shouldShowError)) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (speakers.isNotEmpty()) {
                        val speakerModifier = Modifier.weight(1f)
                        if (speakers.size == 1) {
                            var speakerTextStyle = MaterialTheme.typography.labelMedium
                            if (titleTextStyle.fontSize < speakerTextStyle.fontSize) {
                                speakerTextStyle =
                                    speakerTextStyle.copy(fontSize = titleTextStyle.fontSize)
                            }
                            SingleSpeaker(
                                speaker = speakers.first(),
                                textColor = MaterialTheme.colorScheme.onSurface,
                                textStyle = speakerTextStyle,
                                modifier = speakerModifier,
                            )
                        } else {
                            MultiSpeakers(
                                speakers = speakers,
                                modifier = speakerModifier,
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    if (shouldShowError) {
                        Icon(
                            modifier = Modifier
                                .size(TimetableGridItemSizes.errorHeight),
                            imageVector = Icons.Default.Error,
                            contentDescription = stringResource(SessionsRes.string.content_description_error_icon),
                            tint = MaterialTheme.colorScheme.errorContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleSpeaker(
    speaker: TimetableSpeaker,
    textColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(TimetableGridItemSizes.speakerHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SpeakerIcon(iconUrl = speaker.iconUrl)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = speaker.name,
            style = textStyle,
            color = textColor,
        )
    }
}

@Composable
private fun MultiSpeakers(
    speakers: PersistentList<TimetableSpeaker>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(TimetableGridItemSizes.speakerHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        speakers.forEach { speaker ->
            SpeakerIcon(speaker.iconUrl)
        }
    }
}

@Composable
private fun SpeakerIcon(
    iconUrl: String,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = previewOverride(previewPainter = { rememberVectorPainter(image = Icons.Default.Person) }) {
            rememberAsyncImagePainter(iconUrl)
        },
        contentDescription = stringResource(SessionsRes.string.content_description_user_icon),
        modifier = modifier
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                CircleShape,
            )
            .height(TimetableGridItemSizes.speakerHeight)
            .layout { measurable, constraints ->
                // To keep circle shape, it needs to match the vertical size when pinching in
                val placeable = measurable.measure(constraints)
                val size = placeable.height
                layout(size, size) {
                    placeable.placeRelative(0, 0)
                }
            }
            .clip(CircleShape),
    )
}

/**
 *
 * Calculate the font size and line height of the title by the height of the session grid item.
 *
 * @param textStyle session title text style.
 * @param localDensity local density.
 * @param gridItemHeightPx session grid item height. (unit is px.)
 * @param speaker session speaker.
 * @param titleLength session title length.
 *
 * @return calculated font size and line height. (Both units are sp.)
 *
 */
private fun calculateFontSizeAndLineHeight(
    textStyle: TextStyle,
    localDensity: Density,
    gridItemHeightPx: Int,
    speaker: TimetableSpeaker?,
    titleLength: Int,
): Pair<TextUnit, TextUnit> {
    // The height of the title that should be displayed.
    val scheduleToTitleSpaceHeightPx = with(localDensity) {
        TimetableGridItemSizes.scheduleToTitleSpace.toPx()
    }
    val scheduleHeightPx = with(localDensity) {
        TimetableGridItemSizes.scheduleHeight.toPx()
    }
    val horizontalPaddingPx = with(localDensity) {
        (TimetableGridItemSizes.padding * 2).toPx()
    }
    var displayTitleHeight =
        gridItemHeightPx - scheduleToTitleSpaceHeightPx - scheduleHeightPx - horizontalPaddingPx
    displayTitleHeight -= if (speaker != null) {
        with(localDensity) { TimetableGridItemSizes.speakerHeight.toPx() }
    } else {
        0f
    }

    // Actual height of displayed title.
    val boxWidthWithoutPadding = with(localDensity) {
        (TimetableGridItemSizes.width - TimetableGridItemSizes.padding * 2).toPx()
    }
    val fontSizePx = with(localDensity) { textStyle.fontSize.toPx() }
    val lineHeightPx = with(localDensity) { textStyle.lineHeight.toPx() }
    var actualTitleHeight = calculateTitleHeight(
        fontSizePx = fontSizePx,
        lineHeightPx = lineHeightPx,
        titleLength = titleLength,
        maxWidth = boxWidthWithoutPadding,
    )

    return when {
        displayTitleHeight <= 0 ->
            Pair(TimetableGridItemSizes.minTitleFontSize, TimetableGridItemSizes.minTitleLineHeight)

        displayTitleHeight > actualTitleHeight ->
            Pair(textStyle.fontSize, textStyle.lineHeight)

        else -> {
            // Change the font size until it fits in the height of the title box.
            var fontResizePx = fontSizePx
            var lineHeightResizePx = lineHeightPx

            val minFontSizePx = with(localDensity) {
                TimetableGridItemSizes.minTitleFontSize.toPx()
            }
            val middleLineHeightPx = with(localDensity) {
                TimetableGridItemSizes.middleTitleLineHeight.toPx()
            }
            val minLineHeightPx = with(localDensity) {
                TimetableGridItemSizes.minTitleLineHeight.toPx()
            }

            while (displayTitleHeight <= actualTitleHeight) {
                if (fontResizePx <= minFontSizePx) {
                    fontResizePx = minFontSizePx
                    lineHeightResizePx = minLineHeightPx
                    break
                }

                fontResizePx -= with(localDensity) { 1.sp.toPx() }
                val fontResize = with(localDensity) { fontResizePx.toSp() }
                if (fontResize <= 12.sp && fontResize > 10.sp) {
                    lineHeightResizePx = middleLineHeightPx
                } else if (fontResize <= 10.sp) {
                    lineHeightResizePx = minLineHeightPx
                }
                actualTitleHeight = calculateTitleHeight(
                    fontSizePx = fontResizePx,
                    lineHeightPx = lineHeightResizePx,
                    titleLength = titleLength,
                    maxWidth = boxWidthWithoutPadding,
                )
            }

            Pair(
                with(localDensity) { fontResizePx.toSp() },
                with(localDensity) { lineHeightResizePx.toSp() },
            )
        }
    }
}

/**
 *
 * Calculate the title height.
 *
 * @param fontSizePx font size. (unit is px.)
 * @param lineHeightPx line height. (unit is px.)
 * @param titleLength session title length.
 * @param maxWidth max width of session grid item.
 *
 * @return calculated title height. (unit is px.)
 *
 */
private fun calculateTitleHeight(
    fontSizePx: Float,
    lineHeightPx: Float,
    titleLength: Int,
    maxWidth: Float,
): Float {
    val rows = ceil(titleLength * fontSizePx / maxWidth)
    return fontSizePx + (lineHeightPx * (rows - 1f))
}

object TimetableGridItemSizes {
    val width = 192.dp
    val padding = 12.dp
    val scheduleToTitleSpace = 6.dp
    val scheduleHeight = 16.dp
    val errorHeight = 16.dp
    val speakerHeight = 32.dp
    val minTitleFontSize = 10.sp
    val middleTitleLineHeight = 16.sp // base on MaterialTheme.typography.labelSmall.lineHeight
    val minTitleLineHeight = 12.sp
}

@Preview
@Composable
fun PreviewTimetableGridItem() {
    KaigiTheme {
        Surface {
            TimetableGridItem(
                timetableItem = Session.fake()
                    .copy(speakers = persistentListOf(Session.fake().speakers.first())),
                onTimetableItemClick = {},
                gridItemHeightPx = 350,
            )
        }
    }
}

@Preview
@Composable
fun PreviewTimetableGridLongTitleItem() {
    val fake = Session.fake()

    val localDensity = LocalDensity.current
    val verticalScale = 1f

    val minutePx = with(localDensity) { TimetableSizes.minuteHeight.times(verticalScale).toPx() }
    val displayEndsAt = fake.endsAt.minus(1, DateTimeUnit.MINUTE)
    val height = ((displayEndsAt - fake.startsAt).inWholeMinutes * minutePx).roundToInt()

    KaigiTheme {
        Surface {
            TimetableGridItem(
                timetableItem = Session.fake().let {
                    val longTitle = it.title.copy(
                        jaTitle = it.title.jaTitle.repeat(2),
                        enTitle = it.title.enTitle.repeat(2),
                    )
                    it.copy(title = longTitle, speakers = persistentListOf(it.speakers.first()))
                },
                onTimetableItemClick = {},
                gridItemHeightPx = height,
            )
        }
    }
}

@Preview
@Composable
fun PreviewTimetableGridMultiSpeakersItem() {
    KaigiTheme {
        Surface {
            TimetableGridItem(
                timetableItem = Session.fake(),
                onTimetableItemClick = {},
                gridItemHeightPx = 350,
            )
        }
    }
}

@Preview
@Composable
fun PreviewTimetableGridItemWelcomeTalk() {
    KaigiTheme {
        Surface {
            TimetableGridItem(
                timetableItem = TimetableItem.Special(
                    id = TimetableItemId("1"),
                    title = MultiLangText("ウェルカムトーク", "Welcome Talk"),
                    startsAt = LocalDateTime.parse("2023-09-15T10:30:00")
                        .toInstant(TimeZone.of("UTC+9")),
                    endsAt = LocalDateTime.parse("2023-09-15T10:45:00")
                        .toInstant(TimeZone.of("UTC+9")),
                    category = TimetableCategory(
                        id = 28657,
                        title = MultiLangText("その他", "Other"),
                    ),
                    sessionType = TimetableSessionType.WELCOME_TALK,
                    room = TimetableRoom(3, MultiLangText("Hedgehog", "Hedgehog"), RoomH, 1),
                    targetAudience = "TBW",
                    language = TimetableLanguage(
                        langOfSpeaker = "JAPANESE",
                        isInterpretationTarget = true,
                    ),
                    asset = TimetableAsset(null, null),
                    levels = persistentListOf(
                        "BEGINNER",
                        "INTERMEDIATE",
                        "ADVANCED",
                    ),
                    speakers = persistentListOf(),
                    description = MultiLangText(
                        jaTitle = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n" +
                            "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n",
                        enTitle = "This is a description\nThis is a description\nThis is a description\n" +
                            "This is a description\nThis is a description\nThis is a description\n",
                    ),
                ),
                onTimetableItemClick = {},
                gridItemHeightPx = 154,
            )
        }
    }
}
