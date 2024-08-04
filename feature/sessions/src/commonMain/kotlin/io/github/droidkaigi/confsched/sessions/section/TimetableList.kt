package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.feature.sessions.generated.resources.bookmarked
import conference_app_2024.feature.sessions.generated.resources.image
import conference_app_2024.feature.sessions.generated.resources.not_bookmarked
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.CIRCLE
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.DIAMOND
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.SHARP_DIAMOND
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.SQUARE
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.TimetableListItemBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.TimetableListItemTestTag
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import kotlinx.collections.immutable.PersistentMap
import org.jetbrains.compose.resources.stringResource

const val TimetableListTestTag = "TimetableList"

data class TimetableListUiState(
    val timetableItemMap: PersistentMap<String, List<TimetableItem>>,
    val timetable: Timetable,
)

@Composable
fun TimetableList(
    uiState: TimetableListUiState,
    scrollState: LazyListState,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.testTag(TimetableListTestTag),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = contentPadding,
    ) {
        items(uiState.timetable.timetableItems, key = { it.id.value }) { timetableItem ->
            val isBookmarked = uiState.timetable.bookmarks.contains(timetableItem.id)
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
                    modifier = Modifier
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
                            modifier = Modifier.testTag(TimetableListItemBookmarkIconTestTag),
                        ) {
                            if (isBookmarked) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = stringResource(SessionsRes.string.bookmarked),
                                    tint = Color.Green,
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.FavoriteBorder,
                                    contentDescription = stringResource(SessionsRes.string.not_bookmarked),
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                    Text(
                        text = timetableItem.title.currentLangTitle,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .testTag(TimetableListItemTestTag)
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
                                contentDescription = stringResource(SessionsRes.string.image),
                            )
                            Text(
                                text = speaker.name,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .testTag(TimetableListItemTestTag)
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
    }
}

@Composable
fun TimeTableItemTag(
    tagText: String,
    tagColor: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier
            .border(border = BorderStroke(width = 1.dp, color = tagColor))
            .clip(RoundedCornerShape(15.dp))
            .padding(5.dp),
    ) {
        icon?.let { ico ->
            Icon(ico, "", tint = tagColor)
        }
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            color = tagColor,
            text = tagText,
            modifier = Modifier.padding(end = 5.dp),
        )
    }
}
