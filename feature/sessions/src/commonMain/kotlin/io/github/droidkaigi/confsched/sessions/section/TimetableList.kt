package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCard
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemTag
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableTime
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalAnimatedVisibilityScope
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalClock
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalSharedTransitionScope
import io.github.droidkaigi.confsched.droidkaigiui.icon
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.sessions.component.TimetableNestedScrollStateHolder
import io.github.droidkaigi.confsched.sessions.component.rememberTimetableNestedScrollConnection
import io.github.droidkaigi.confsched.sessions.component.rememberTimetableNestedScrollStateHolder
import io.github.droidkaigi.confsched.sessions.section.TimetableListUiState.TimeSlot
import io.github.droidkaigi.confsched.sessions.timetableDetailSharedContentStateKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

const val TimetableListTestTag = "TimetableList"

data class TimetableListUiState(
    val timetableItemMap: PersistentMap<TimeSlot, List<TimetableItem>>,
    val timetable: Timetable,
) {
    data class TimeSlot(
        val startTime: LocalTime,
        val endTime: LocalTime,
    ) {
        val startTimeString: String get() = startTime.toTimetableTimeString()
        val endTimeString: String get() = endTime.toTimetableTimeString()

        val key: String get() = "$startTime-$endTime"

        private fun LocalTime.toTimetableTimeString(): String {
            return "$hour".padStart(2, '0') + ":" + "$minute".padStart(2, '0')
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun TimetableList(
    uiState: TimetableListUiState,
    scrollState: LazyListState,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    onTimetableItemClick: (TimetableItem) -> Unit,
    contentPadding: PaddingValues,
    timetableItemTagsContent: @Composable RowScope.(TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    nestedScrollStateHolder: TimetableNestedScrollStateHolder = rememberTimetableNestedScrollStateHolder(true),
    highlightWord: String = "",
    enableAutoScrolling: Boolean = true,
    scrolledToCurrentTimeState: ScrolledToCurrentTimeState = ScrolledToCurrentTimeState(),
) {
    val layoutDirection = LocalLayoutDirection.current
    val clock = LocalClock.current
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedScope = LocalAnimatedVisibilityScope.current
    val windowSize = calculateWindowSizeClass()
    val isWideWidthScreen = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium -> true
        WindowWidthSizeClass.Expanded -> true
        else -> false
    }
    val columnNum by remember { derivedStateOf { if (isWideWidthScreen) 2 else 1 } }

    val nestedScrollConnection = rememberTimetableNestedScrollConnection(
        nestedScrollStateHolder = nestedScrollStateHolder,
    )

    LaunchedEffect(Unit) {
        if (enableAutoScrolling && scrolledToCurrentTimeState.inTimetableList.not()) {
            val progressingSessionIndex = uiState.timetableItemMap.keys
                .insertDummyEndOfTheDayItem() // Insert dummy at a position after last session to allow scrolling
                .windowed(2, 1, true)
                .indexOfFirst { clock.now().toLocalTime() in it.first().startTime..<it.last().startTime }

            progressingSessionIndex.takeIf { it != -1 }?.let {
                scrollState.scrollToItem(it)
            }
            scrolledToCurrentTimeState.scrolledInTimetableList()
        }
    }

    LazyColumn(
        modifier = modifier.testTag(TimetableListTestTag)
            .offset {
                IntOffset(x = 0, y = nestedScrollStateHolder.uiState.dayTabOffsetY.toInt())
            }
            .nestedScroll(nestedScrollConnection),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding(),
            start = 16.dp + contentPadding.calculateStartPadding(layoutDirection),
            end = 16.dp + contentPadding.calculateEndPadding(layoutDirection),
        ),
    ) {
        itemsIndexed(
            // TODO: Check whether the number of recompositions increases.
            items = uiState.timetableItemMap.toList(),
            key = { _, item -> item.first.key },
        ) { index, (time, timetableItems) ->
            var rowHeight by remember { mutableIntStateOf(0) }
            var timeTextHeight by remember { mutableIntStateOf(0) }
            val timeTextOffset by remember(scrollState) {
                derivedStateOf {
                    val maxOffset = rowHeight - timeTextHeight
                    if (index == scrollState.firstVisibleItemIndex) {
                        minOf(scrollState.firstVisibleItemScrollOffset, maxOffset).coerceAtLeast(0)
                    } else {
                        0
                    }
                }
            }
            Row(
                modifier = Modifier
                    .onGloballyPositioned {
                        rowHeight = it.size.height
                    },
            ) {
                TimetableTime(
                    modifier = Modifier
                        .onGloballyPositioned {
                            timeTextHeight = it.size.height
                        }
                        .offset { IntOffset(0, timeTextOffset) },
                    startTime = time.startTimeString,
                    endTime = time.endTimeString,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    timetableItems.windowed(columnNum, columnNum, true).forEach { windowedItems ->
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            windowedItems.onEach { timetableItem ->
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
                                                key = timetableDetailSharedContentStateKey(
                                                    timetableItemId = timetableItem.id,
                                                ),
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
                                    modifier = timetableItemCardModifier
                                        .weight(1F)
                                        .fillMaxHeight(),
                                    highlightWord = highlightWord,
                                    tags = {
                                        TimetableItemTag(
                                            tagText = timetableItem.room.name.currentLangTitle,
                                            icon = timetableItem.room.icon,
                                            tagColor = LocalRoomTheme.current.primaryColor,
                                            modifier = Modifier.background(LocalRoomTheme.current.containerColor),
                                        )
                                        timetableItemTagsContent(timetableItem)
                                    },
                                    onTimetableItemClick = onTimetableItemClick,
                                )
                            }
                            if (windowedItems.size < columnNum) {
                                repeat(columnNum - windowedItems.size) {
                                    Spacer(modifier = Modifier.weight((columnNum - windowedItems.size).toFloat()))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun ImmutableSet<TimeSlot>.insertDummyEndOfTheDayItem(): ImmutableSet<TimeSlot> {
    val endOfTheDayInstant = LocalTime(23, 59, 59)
    return plus(
        TimeSlot(
            startTime = endOfTheDayInstant,
            endTime = endOfTheDayInstant,
        ),
    ).toImmutableSet()
}

private fun Instant.toLocalTime(): LocalTime {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.time
}
