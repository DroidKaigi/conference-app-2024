package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.sessions.component.TimetableTime
import io.github.droidkaigi.confsched.sessions.timetableDetailSharedContentStateKey
import io.github.droidkaigi.confsched.ui.component.TimetableItemCard
import io.github.droidkaigi.confsched.ui.component.TimetableItemTag
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalAnimatedVisibilityScope
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalSharedTransitionScope
import io.github.droidkaigi.confsched.ui.icon
import kotlinx.collections.immutable.PersistentMap

const val TimetableListTestTag = "TimetableList"

data class TimetableListUiState(
    val timetableItemMap: PersistentMap<TimeSlot, List<TimetableItem>>,
    val timetable: Timetable,
) {
    data class TimeSlot(
        val startTimeString: String,
        val endTimeString: String,
    ) {
        val key: String get() = "$startTimeString-$endTimeString"
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TimetableList(
    uiState: TimetableListUiState,
    scrollState: LazyListState,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    highlightWord: String = "",
) {
    val layoutDirection = LocalLayoutDirection.current
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedScope = LocalAnimatedVisibilityScope.current

    LazyColumn(
        modifier = modifier.testTag(TimetableListTestTag),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding(),
            start = 16.dp + contentPadding.calculateStartPadding(layoutDirection),
            end = 16.dp + contentPadding.calculateEndPadding(layoutDirection),
        ),
    ) {
        items(
            // TODO: Check whether the number of recompositions increases.
            items = uiState.timetableItemMap.toList(),
            key = { it.first.key },
        ) { (time, timetableItems) ->
            Row {
                TimetableTime(
                    startTime = time.startTimeString,
                    endTime = time.endTimeString,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    timetableItems.onEach { timetableItem ->
                        val isBookmarked =
                            uiState.timetable.bookmarks.contains(timetableItem.id)
                        val timetableItemCardModifier = if (
                            !scrollState.isScrollInProgress &&
                            sharedTransitionScope != null &&
                            animatedScope != null
                        ) {
                            with(sharedTransitionScope) {
                                Modifier.sharedElement(
                                    state = rememberSharedContentState(
                                        key = timetableDetailSharedContentStateKey(timetableItemId = timetableItem.id),
                                    ),
                                    animatedVisibilityScope = animatedScope,
                                )
                            }
                        } else {
                            Modifier
                        }
                        TimetableItemCard(
                            isBookmarked = isBookmarked,
                            timetableItem = timetableItem,
                            onBookmarkClick = onBookmarkClick,
                            modifier = timetableItemCardModifier,
                            highlightWord = highlightWord,
                            tags = {
                                TimetableItemTag(
                                    tagText = timetableItem.room.name.currentLangTitle,
                                    icon = timetableItem.room.icon,
                                    tagColor = LocalRoomTheme.current.primaryColor,
                                    modifier = Modifier.background(LocalRoomTheme.current.containerColor),
                                )
                                timetableItem.language.labels.forEach { label ->
                                    TimetableItemTag(
                                        tagText = label,
                                        tagColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            },
                            onTimetableItemClick = onTimetableItemClick,
                        )
                    }
                }
            }
        }
    }
}