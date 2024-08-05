package io.github.droidkaigi.confsched.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
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
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.CIRCLE
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.DIAMOND
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.SHARP_DIAMOND
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.SQUARE
import io.github.droidkaigi.confsched.ui.UiRes
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import org.jetbrains.compose.resources.stringResource

const val TimetableItemCardBookmarkIconTestTag = "TimetableListItemBookmarkIcon"
const val TimetableItemCardTestTag = "TimetableListItem"

@Composable
fun TimeTableItemCard(
    modifier: Modifier = Modifier,
    isBookmarked: Boolean,
    timetableItem: TimetableItem,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
) {
    val roomName = timetableItem.room.name.currentLangTitle

    // TODO: Replace with the real icons. Probably need to embed them.
    val roomIcon = when (timetableItem.room.getShape()) {
        SQUARE -> {
            Icons.Filled.Square
        }

        CIRCLE -> {
            Icons.Filled.Circle
        }

        DIAMOND -> {
            Icons.Filled.Thermostat
        }

        SHARP_DIAMOND -> {
            Icons.Filled.Diamond
        }

        else -> {
            Icons.Filled.Star
        }
    }
    ProvideRoomTheme(timetableItem.room.getThemeKey()) {
        Column(
            modifier = modifier
                .border(
                    border = BorderStroke(width = 1.dp, color = Color.White),
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(15.dp),
        ) {
            Row {
                TimeTableItemTag(tagText = roomName, icon = roomIcon, tagColor = LocalRoomTheme.current.primaryColor)
                Spacer(modifier = Modifier.padding(3.dp))
                timetableItem.language.labels.forEach { label ->
                    TimeTableItemTag(tagText = label, tagColor = Color.White)
                    Spacer(modifier = Modifier.padding(3.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { onBookmarkClick(timetableItem, true) },
                    modifier = Modifier.testTag(TimetableItemCardBookmarkIconTestTag),
                ) {
                    if (isBookmarked) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = stringResource(UiRes.string.bookmarked),
                            tint = Color.Green,
                        )
                    } else {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = stringResource(UiRes.string.not_bookmarked),
                            tint = Color.White,
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
