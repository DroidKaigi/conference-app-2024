package io.github.droidkaigi.confsched.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.core.ui.generated.resources.bookmarked
import conference_app_2024.core.ui.generated.resources.image
import conference_app_2024.core.ui.generated.resources.not_bookmarked
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.model.TimetableItem
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
        Column(
            modifier = modifier
                .border(
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(15.dp),
        ) {
            Box {
                Row(content = tags)
                TextButton(
                    onClick = { onBookmarkClick(timetableItem, true) },
                    modifier = Modifier
                        .testTag(TimetableItemCardBookmarkButtonTestTag)
                        .align(Alignment.TopEnd),
                ) {
                    if (isBookmarked) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = stringResource(UiRes.string.bookmarked),
                            tint = Color.Green,
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

            Text(
                text = timetableItem.title.currentLangTitle,
                fontSize = 24.sp,
                modifier = Modifier
                    .testTag(TimetableItemCardTestTag)
                    .padding(bottom = 5.dp)
                    .clickable { onTimetableItemClick(timetableItem) },
            )
            timetableItem.speakers.forEach { speaker ->
                Row {
                    // TODO: Fixed image loading again but its still slow. Maybe we need smaller images?
                    val painter = rememberAsyncImagePainter(speaker.iconUrl)
                    Image(
                        painter = painter,
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp)
                            .clip(CircleShape),
                        contentDescription = stringResource(UiRes.string.image),
                    )
                    Text(
                        text = speaker.name,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .testTag(TimetableItemCardTestTag)
                            .padding(5.dp)
                            .align(Alignment.CenterVertically),
                    )
                    // TODO: Message goes here (missing from object we can access here?)
                }
            }
        }
        // TODO: There is no data for the warning string right now. (Should go here)
    }
}
