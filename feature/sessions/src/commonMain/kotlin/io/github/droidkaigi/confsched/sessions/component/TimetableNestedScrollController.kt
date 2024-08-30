package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun rememberTimetableNestedScrollController(
    isListTimetable: Boolean,
): TimetableNestedScrollController {
    return remember(isListTimetable) {
        TimetableNestedScrollControllerImpl(
            isListTimetable = isListTimetable,
        )
    }
}

internal sealed interface TimetableNestedScrollController {
    val dayTabHeight: Float
    val dayTabOffsetY: Float
    fun setDayTabHeight(height: Float)
    fun updateDayTabOffsetY(offset: Float)
}

private class TimetableNestedScrollControllerImpl(
    isListTimetable: Boolean,
) : TimetableNestedScrollController {
    private var _dayTabHeight: MutableState<Float> = mutableStateOf(0f)
    override val dayTabHeight: Float by _dayTabHeight

    private var _dayTabOffsetY: MutableState<Float> = mutableStateOf(0f)
    override val dayTabOffsetY: Float by derivedStateOf {
        if (isListTimetable) {
            _dayTabOffsetY.value
        } else {
            0f
        }
    }

    override fun setDayTabHeight(height: Float) {
        _dayTabHeight.value = height
    }

    override fun updateDayTabOffsetY(offset: Float) {
        _dayTabOffsetY.value = offset
    }
}
