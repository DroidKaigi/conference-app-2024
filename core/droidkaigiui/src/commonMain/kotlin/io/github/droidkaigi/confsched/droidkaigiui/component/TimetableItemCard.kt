package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.core.droidkaigiui.generated.resources.bookmarked
import conference_app_2024.core.droidkaigiui.generated.resources.image
import conference_app_2024.core.droidkaigiui.generated.resources.not_bookmarked
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.primaryFixed
import io.github.droidkaigi.confsched.droidkaigiui.DroidKaigiUiRes
import io.github.droidkaigi.confsched.droidkaigiui.animation.LocalFavoriteAnimationScope
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import org.jetbrains.compose.resources.stringResource

const val TimetableItemCardBookmarkButtonTestTag = "TimetableItemCardBookmarkButton"
const val TimetableItemCardBookmarkedIconTestTag = "TimetableItemCardBookmarkedIcon"
const val TimetableItemCardTestTag = "TimetableListItem"
const val TimetableItemCardTitleTextTestTag = "TimetableItemCardTitleText"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimetableItemCard(
    isBookmarked: Boolean,
    timetableItem: TimetableItem,
    tags: @Composable RowScope.() -> Unit,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    highlightWord: String = "",
) {
    val haptic = LocalHapticFeedback.current

    val highlightBackgroundColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.14f)
    val annotatedTitleString = remember(
        timetableItem.title.currentLangTitle,
        highlightBackgroundColor,
        highlightWord,
    ) {
        buildAnnotatedTitleString(
            title = timetableItem.title.currentLangTitle,
            highlightWord = highlightWord,
            highlightBackgroundColor = highlightBackgroundColor,
        )
    }

    ProvideRoomTheme(timetableItem.room.getThemeKey()) {
        Row(
            modifier = modifier
                .testTag(TimetableItemCardTestTag)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                    ),
                    shape = RoundedCornerShape(4.dp),
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = LocalRoomTheme.current.primaryColor),
                    onClick = { onTimetableItemClick(timetableItem) },
                ),
        ) {
            val contentPadding = 12.dp
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = contentPadding, start = contentPadding, bottom = contentPadding),
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = tags,
                )
                Text(
                    text = annotatedTitleString,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .testTag(TimetableItemCardTitleTextTestTag)
                        .padding(top = 8.dp),
                )
                if (timetableItem.speakers.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))

                    timetableItem.speakers.forEach { speaker ->
                        Row(
                            modifier = Modifier.height(36.dp).padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            // TODO: Fixed image loading again but its still slow. Maybe we need smaller images?
                            val painter = rememberAsyncImagePainter(speaker.iconUrl)
                            Image(
                                painter = painter,
                                modifier = Modifier
                                    .width(32.dp)
                                    .height(32.dp)
                                    .clip(CircleShape)
                                    .border(
                                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                        CircleShape,
                                    ),
                                contentDescription = stringResource(DroidKaigiUiRes.string.image),
                            )
                            Text(
                                text = speaker.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .testTag(TimetableItemCardTestTag)
                                    .align(Alignment.CenterVertically),
                            )
                            // TODO: Message goes here (missing from object we can access here?)
                        }
                    }
                }
                if (timetableItem is Session) {
                    timetableItem.message?.let {
                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = stringResource(DroidKaigiUiRes.string.image),
                                tint = MaterialTheme.colorScheme.error,
                            )
                            Text(
                                text = it.currentLangTitle,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }

            FavoriteButton(
                isBookmarked = isBookmarked,
                onClick = {
                    if (!isBookmarked) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }

                    onBookmarkClick(timetableItem, true)
                },
            )
        }
        // TODO: There is no data for the warning string right now. (Should go here)
    }
}

@Composable
private fun FavoriteButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
) {
    val animationScope = LocalFavoriteAnimationScope.current
    var favoriteButtonOffset = Offset.Zero

    TextButton(
        onClick = {
            onClick()
            if (!isBookmarked) {
                animationScope.startAnimation(favoriteButtonOffset)
            }
        },
        modifier = Modifier
            .testTag(TimetableItemCardBookmarkButtonTestTag)
            .onGloballyPositioned { coordinates ->
                favoriteButtonOffset = coordinates.positionInRoot()
            },
    ) {
        if (isBookmarked) {
            Icon(
                Filled.Favorite,
                contentDescription = stringResource(DroidKaigiUiRes.string.bookmarked),
                tint = MaterialTheme.colorScheme.primaryFixed,
                modifier = Modifier
                    .testTag(TimetableItemCardBookmarkedIconTestTag),
            )
        } else {
            Icon(
                Outlined.FavoriteBorder,
                contentDescription = stringResource(DroidKaigiUiRes.string.not_bookmarked),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun buildAnnotatedTitleString(
    title: String,
    highlightWord: String,
    highlightBackgroundColor: Color,
): AnnotatedString {
    return buildAnnotatedString {
        append(title)

        if (highlightWord.isEmpty()) return@buildAnnotatedString

        val highlightRanges = mutableListOf<IntRange>()
        var startIndex = 0
        while (true) {
            startIndex = title.indexOf(
                string = highlightWord,
                startIndex = startIndex,
                ignoreCase = true,
            )
            if (startIndex == -1) {
                break
            } else {
                highlightRanges += IntRange(
                    startIndex,
                    startIndex + highlightWord.length,
                )
                startIndex += highlightWord.length
            }
        }

        val highlightStyle = SpanStyle(
            textDecoration = TextDecoration.Underline,
            background = highlightBackgroundColor,
        )

        highlightRanges.forEach { range ->
            addStyle(
                style = highlightStyle,
                start = range.first,
                end = range.last,
            )
        }
    }
}
