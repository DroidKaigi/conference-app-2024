package io.github.droidkaigi.confsched.eventmap.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.eventmap.generated.resources.Res
import conference_app_2024.feature.eventmap.generated.resources.event_map_1f
import conference_app_2024.feature.eventmap.generated.resources.event_map_b1f
import io.github.droidkaigi.confsched.model.FloorLevel
import org.jetbrains.compose.resources.painterResource

const val EventMapTabTestTagPrefix = "EventMapTabTestTag:"
const val EventMapTabImageTestTag = "EventMapTabImageTestTag"

@Composable
fun EventMapTab(
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FloorLevel.entries.reversed().forEachIndexed { index, floorLevel ->
                EventMapChip(
                    modifier = Modifier.testTag(EventMapTabTestTagPrefix.plus(floorLevel.floorName)),
                    selected = selectedTabIndex == index,
                    text = floorLevel.floorName,
                    onClick = { selectedTabIndex = index },
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        val mapRes = if (selectedTabIndex == 0) {
            Res.drawable.event_map_1f
        } else {
            Res.drawable.event_map_b1f
        }
        val mapContentDescription = if (selectedTabIndex == 0) {
            FloorLevel.Ground.floorName
        } else {
            FloorLevel.Basement.floorName
        }
        Image(
            modifier = Modifier.testTag(EventMapTabImageTestTag),
            painter = painterResource(mapRes),
            contentDescription = "Map of $mapContentDescription",
        )
    }
}

@Composable
private fun EventMapChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        leadingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                )
            }
        },
    )
}
