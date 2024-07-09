package io.github.droidkaigi.confsched.eventmap.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import conference_app_2024.feature.eventmap.generated.resources.Res
import conference_app_2024.feature.eventmap.generated.resources.event_map
import org.jetbrains.compose.resources.painterResource

@Composable
fun EventMapTab() {
    val selectedColor = Color(0xFF4AFF82)
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        TabRow(
            modifier = Modifier.width(80.dp),
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
                Tab(
                    modifier = Modifier.height(64.dp),
                    selected = true,
                    onClick = {
                        selectedTabIndex = 0
                    },
                    selectedContentColor = selectedColor,
                    unselectedContentColor = Color.White,
                ) {
                    FloorText(
                        text = "1F",
                        isSelected = selectedTabIndex == 0,
                    )
                }
                Tab(
                    modifier = Modifier.height(64.dp),
                    selected = false,
                    onClick = {
                        selectedTabIndex = 1
                    },
                    selectedContentColor = selectedColor,
                    unselectedContentColor = Color.White,
                ) {
                    FloorText(
                        text = "2F",
                        isSelected = selectedTabIndex == 1,
                    )
                }
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(Res.drawable.event_map),
            contentDescription = null,
        )
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
        fontSize = 24.sp,
        lineHeight = 23.8.sp,
        color = if (isSelected) {
            Color(0xFF4AFF82)
        } else {
            Color.White
        },
    )
}
