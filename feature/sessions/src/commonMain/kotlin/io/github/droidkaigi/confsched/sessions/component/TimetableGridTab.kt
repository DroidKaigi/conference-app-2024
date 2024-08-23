package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.sessions.section.TimetableTabTestTag
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableDayTab(
    selectedDay: DroidKaigi2024Day,
    onDaySelected: (day: DroidKaigi2024Day) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val tabWidth = with(density) {
        // calculate width from sp
        // NOTE: 6 is a magic number here
        ((16.sp * 6) * fontScale).toDp()
    }
    val selectedTabIndex = selectedDay.tabIndex()
    val selectedColor = Color(0xFF4AFF82)
    Column(
        modifier = modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        TabRow(
            modifier = Modifier.width(tabWidth),
            selectedTabIndex = selectedTabIndex,
            indicator = @Composable { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .wrapContentWidth()
                            .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = selectedColor,
                    )
                }
            },
            tabs = {
                DroidKaigi2024Day.visibleDays().forEach { conferenceDay ->
                    val isSelected = conferenceDay == selectedDay
                    Tab(
                        modifier = Modifier
                            .testTag(TimetableTabTestTag.plus(conferenceDay.ordinal))
                            .requiredHeightIn(min = 26.dp),
                        selected = isSelected,
                        onClick = {
                            onDaySelected(conferenceDay)
                        },
                        selectedContentColor = selectedColor,
                        unselectedContentColor = Color.White,
                    ) {
                        FloorText(
                            text = conferenceDay.monthAndDay(),
                            isSelected = isSelected,
                        )
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FloorText(
    text: String,
    isSelected: Boolean,
) {
    Text(
        modifier = Modifier.padding(bottom = 4.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 16.sp,
        lineHeight = 23.8.sp,
        color = if (isSelected) {
            Color(0xFF4AFF82)
        } else {
            Color.White
        },
    )
}

@Preview
@Composable
fun PreviewTimetableDayTab() {
    KaigiTheme {
        Surface {
            TimetableDayTab(
                selectedDay = DroidKaigi2024Day.ConferenceDay1,
                onDaySelected = {},
            )
        }
    }
}
