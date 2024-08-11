package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.feature.sessions.generated.resources.conference
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.sessions.SessionsRes
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimetableDayTab(
    selectedDay: DroidKaigi2024Day,
    onDaySelected: (day: DroidKaigi2024Day) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedTabIndex = selectedDay.tabIndex()
    val selectedColor = Color(0xFF4AFF82)
    Column(
        modifier = modifier.padding(start = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        TabRow(
            modifier = Modifier.width(350.dp),
            selectedTabIndex = selectedTabIndex,
            indicator = @Composable { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = selectedColor,
                    )
                }
            },
            tabs = {
                DroidKaigi2024Day.visibleDays().forEach { conferenceDay ->
                    Tab(
                        modifier = Modifier.height(64.dp),
                        selected = false,
                        onClick = {
                            onDaySelected(conferenceDay)
                        },
                        selectedContentColor = selectedColor,
                        unselectedContentColor = Color.White,
                    ) {
                        val isSelected = conferenceDay == selectedDay
                        if (isSelected) {
                            FloorText(
                                text = stringResource(SessionsRes.string.conference),
                                isSelected = isSelected,
                            )
                            Text(
                                text = " (${conferenceDay.monthAndDay()})",
                                fontSize = 11.sp,
                                color = Color(0xFF4AFF82),
                            )
                        } else {
                            FloorText(
                                text = conferenceDay.monthAndDay(),
                                isSelected = isSelected,
                            )
                        }
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
        text = text,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 23.8.sp,
        color = if (isSelected) {
            Color(0xFF4AFF82)
        } else {
            Color.White
        },
    )
}
