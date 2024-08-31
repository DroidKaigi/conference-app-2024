package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

internal data class TimetableNestedScrollConnection(
    val flingBehavior: FlingBehavior,
    val minOffset: () -> Float,
    val currentOffset: () -> Float,
    val onOffsetChange: (Float) -> Unit,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return dispatchScroll(
            available = available,
        )
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val outerScopeScroll: (Offset) -> Offset = { delta ->
            dispatchScroll(delta)
        }

        val scrollScope = object : ScrollScope {
            override fun scrollBy(pixels: Float): Float {
                val delta = Offset(x = 0f, y = pixels)
                val consumed = outerScopeScroll(delta)
                return consumed.y
            }
        }

        var consumedVelocity: Velocity
        with(scrollScope) {
            with(flingBehavior) {
                val remainingVelocity = available.copy(
                    y = performFling(available.y),
                )
                consumedVelocity = available - remainingVelocity
            }
        }

        return consumedVelocity
    }

    /**
     * Consume scroll event and update offset.
     * @param available available scroll offset
     * @return consumed scroll offset
     */
    private fun dispatchScroll(available: Offset): Offset {
        val availableY = available.y

        val currentOffset = currentOffset()
        val minOffset = minOffset().coerceAtMost(0f)

        val nextOffset = (currentOffset + availableY).coerceIn(minOffset, 0f)
        val consumed = nextOffset - currentOffset

        onOffsetChange(nextOffset)

        return Offset(x = 0f, y = consumed)
    }
}

@Composable
internal fun rememberTimetableNestedScrollConnection(
    nestedScrollStateHolder: TimetableNestedScrollStateHolder,
): TimetableNestedScrollConnection {
    val flingBehavior = rememberFlingBehavior()

    return remember(nestedScrollStateHolder, flingBehavior) {
        TimetableNestedScrollConnection(
            flingBehavior = flingBehavior,
            minOffset = { -nestedScrollStateHolder.uiState.scrollConnectionMinOffset },
            currentOffset = { nestedScrollStateHolder.uiState.dayTabOffsetY },
            onOffsetChange = { nestedScrollStateHolder.onTimetableListOffsetChanged(it) },
        )
    }
}
