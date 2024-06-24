package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.CIRCLE
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.DIAMOND
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.SHARP_DIAMOND
import io.github.droidkaigi.confsched.model.TimetableRoom.Shapes.SQUARE
import io.github.droidkaigi.confsched.sessions.TimetableListItemBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.TimetableListItemTestTag
import kotlinx.collections.immutable.PersistentMap

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
        contentPadding = contentPadding,
    ) {
        items(uiState.timetable.timetableItems, key = { it.id.value }) { timetableItem ->
            val isBookmarked = uiState.timetable.bookmarks.contains(timetableItem.id)
            val roomName = timetableItem.room.name.currentLangTitle

            //TODO: Replace with the real icons. Probably need to embed them.
            val roomIcon = when (timetableItem.room.getShape()) {
                SQUARE -> { Icons.Filled.Square }
                CIRCLE -> { Icons.Filled.Circle }
                DIAMOND -> {Icons.Filled.Thermostat }
                SHARP_DIAMOND -> {Icons.Filled.Diamond }
                else -> { Icons.Filled.Star }
            }
            val roomColor = timetableItem.room.getColor()

            Column(
                modifier = Modifier.border(border= BorderStroke(width=1.dp,color=Color.White))
                    .clip(RoundedCornerShape(5.dp))
                    .padding(15.dp)) {
                Row {
                    TagView(tagText=roomName, icon = roomIcon, tagColor=roomColor)
                    Spacer(modifier = Modifier.padding(3.dp))
                    TagView(tagText="JA", tagColor=Color.White)
                    Spacer(modifier = Modifier.padding(3.dp))
                    TagView(tagText="EN", tagColor=Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { onBookmarkClick(timetableItem, true) },
                        modifier = Modifier.testTag(TimetableListItemBookmarkIconTestTag)
                    ) {
                        if (isBookmarked) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Bookmarked",
                                tint = Color.Green
                            )
                        } else {
                            Icon(
                                Icons.Outlined.FavoriteBorder,
                                contentDescription = "Not Bookmarked",
                                tint = Color.White
                            )
                        }
                    }
                }
                Text(
                    text = timetableItem.title.currentLangTitle,
                    modifier = Modifier
                        .testTag(TimetableListItemTestTag)
                        .clickable { onTimetableItemClick(timetableItem) },
                )
            }
        }
    }
}

@Composable
fun TagView (tagText: String, icon: ImageVector?=null, tagColor: Color) {
    Row(modifier = Modifier
        .border(border = BorderStroke(width = 1.dp, color = tagColor))
        .clip(RoundedCornerShape(15.dp))
        .padding(5.dp)) {
        icon?.let { ico ->
            Icon(ico, "", tint=tagColor)
        }
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            color = tagColor,
            text = tagText,
            modifier = Modifier.padding(end=5.dp)
        )
    }
}

//@Composable
//fun testItem() {
//    val isBookmarked = false
//    Row {
//        Text(
//            text = "Placeholder",//timetableItem.title.currentLangTitle,
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                //.testTag(TimetableListItemTestTag)
//                //.clickable { onTimetableItemClick(timetableItem) },
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        TextButton(
//            onClick = {}
//            //onClick = { onBookmarkClick(timetableItem, true) }//,
//            //modifier = Modifier.testTag(TimetableListItemBookmarkIconTestTag)
//        ) {
//            if (isBookmarked) {
//                Icon(Icons.Filled.Favorite, contentDescription = "Bookmarked", tint= Color.Green)
//            } else {
//                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Not Bookmarked", tint= Color.White)
//            }
//        }
//    }
//}
//
//@Preview @Composable
//fun previewItem() {
//    testItem()
//}


