package io.github.droidkaigi.confsched.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.core.ui.generated.resources.bookmarked
import conference_app_2024.core.ui.generated.resources.image
import conference_app_2024.core.ui.generated.resources.not_bookmarked
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.primaryFixed
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.ui.UiRes
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import org.jetbrains.compose.resources.stringResource

const val TimetableItemCardBookmarkButtonTestTag = "TimetableItemCardBookmarkButton"
const val TimetableItemCardBookmarkedIconTestTag = "TimetableItemCardBookmarkedIcon"
const val TimetableItemCardTestTag = "TimetableListItem"

@Composable
fun TimetableItemCard(
    isBookmarked: Boolean,
    timetableItem: TimetableItem,
    tags: @Composable RowScope.() -> Unit,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onTimetableItemClick: (TimetableItem) -> Unit,
) {
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
                .clickable { onTimetableItemClick(timetableItem) },
        ) {
            val contentPadding = 12.dp
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = contentPadding, start = contentPadding, bottom = contentPadding),
            ) {
                Row(content = tags)
                Text(
                    text = timetableItem.title.currentLangTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .testTag(TimetableItemCardTestTag)
                        .padding(top = 8.dp),
                )
                if (timetableItem.speakers.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))

                    timetableItem.speakers.forEach { speaker ->
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
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
                                contentDescription = stringResource(UiRes.string.image),
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
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = stringResource(UiRes.string.image),
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
            TextButton(
                onClick = { onBookmarkClick(timetableItem, true) },
                modifier = Modifier.testTag(TimetableItemCardBookmarkButtonTestTag),
            ) {
                if (isBookmarked) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = stringResource(UiRes.string.bookmarked),
                        tint = MaterialTheme.colorScheme.primaryFixed,
                        modifier = Modifier
                            .testTag(TimetableItemCardBookmarkedIconTestTag),
                    )
                } else {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(UiRes.string.not_bookmarked),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        // TODO: There is no data for the warning string right now. (Should go here)
    }
}
