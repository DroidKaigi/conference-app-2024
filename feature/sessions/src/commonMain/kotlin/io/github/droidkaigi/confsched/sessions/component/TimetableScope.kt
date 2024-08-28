package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun rememberTimetableScope(
    isListTimetable: Boolean,
): TimetableScope {
    return remember(isListTimetable) {
        TimetableScopeImpl(
            isListTimetable = isListTimetable,
        )
    }
}

@Composable
internal fun WithTimetableScope(
    scope: TimetableScope = rememberTimetableScope(isListTimetable = true),
    content: @Composable TimetableScope.() -> Unit,
) {
    scope.content()
}

internal sealed interface TimetableScope {
    val dayTabHeight: Float
    val dayTabOffsetY: Float
    fun setDayTabHeight(height: Float)
    fun updateDayTabOffsetY(offset: Float)
}

private class TimetableScopeImpl(
    isListTimetable: Boolean,
) : TimetableScope {
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
