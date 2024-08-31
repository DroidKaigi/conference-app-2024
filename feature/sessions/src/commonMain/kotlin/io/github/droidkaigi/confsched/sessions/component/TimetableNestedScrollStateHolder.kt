package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun rememberTimetableNestedScrollStateHolder(
    isListTimetable: Boolean,
): TimetableNestedScrollStateHolder {
    return remember(isListTimetable) {
        TimetableNestedScrollStateHolderImpl(
            isListTimetable = isListTimetable,
        )
    }
}

internal data class TimetableNestedScrollUiState(
    val scrollConnectionMinOffset: Float,
    val dayTabOffsetY: Float,
)

internal sealed interface TimetableNestedScrollStateHolder {
    val uiState: TimetableNestedScrollUiState
    fun onDayTabHeightMeasured(height: Float)
    fun onTimetableListOffsetChanged(offset: Float)
}

private class TimetableNestedScrollStateHolderImpl(
    isListTimetable: Boolean,
) : TimetableNestedScrollStateHolder {
    private var _scrollConnectionMinOffset: MutableState<Float> = mutableStateOf(0f)
    private val scrollConnectionMinOffset: Float by _scrollConnectionMinOffset

    private var _dayTabOffsetY: MutableState<Float> = mutableStateOf(0f)
    private val dayTabOffsetY: Float by derivedStateOf {
        if (isListTimetable) {
            _dayTabOffsetY.value
        } else {
            0f
        }
    }

    override val uiState: TimetableNestedScrollUiState by derivedStateOf {
        TimetableNestedScrollUiState(
            scrollConnectionMinOffset = scrollConnectionMinOffset,
            dayTabOffsetY = dayTabOffsetY,
        )
    }

    override fun onDayTabHeightMeasured(height: Float) {
        _scrollConnectionMinOffset.value = height
    }

    override fun onTimetableListOffsetChanged(offset: Float) {
        _dayTabOffsetY.value = offset
    }
}
