package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

internal data class TimetableNestedScrollConnection(
    val minOffset: () -> Float,
    val currentOffset: () -> Float,
    val onOffsetChange: (Float) -> Unit,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val availableY = available.y

        val currentOffset = currentOffset()
        val minOffset = minOffset()

        val nextOffset = (currentOffset + availableY).coerceIn(minOffset, 0f)
        val consumed = nextOffset - currentOffset

        onOffsetChange(nextOffset)

        return if (availableY < 0f) {
            Offset(x = 0f, y = consumed)
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        return super.onPostScroll(consumed, available, source)
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        return super.onPreFling(available)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        return super.onPostFling(consumed, available)
    }
}

@Composable
internal fun rememberTimetableNestedScrollConnection(
    timetableScope: TimetableScope,
): TimetableNestedScrollConnection {
    return remember(timetableScope) {
        TimetableNestedScrollConnection(
            minOffset = { -timetableScope.dayTabHeight },
            currentOffset = { timetableScope.dayTabOffsetY },
            onOffsetChange = { timetableScope.updateDayTabOffset(it) },
        )
    }
}
