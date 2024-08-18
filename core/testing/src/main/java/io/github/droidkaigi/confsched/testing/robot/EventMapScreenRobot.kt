package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.eventmap.EventMapItemTestTag
import io.github.droidkaigi.confsched.eventmap.EventMapLazyColumnTestTag
import io.github.droidkaigi.confsched.eventmap.EventMapScreen
import io.github.droidkaigi.confsched.eventmap.component.EventMapTabImageTestTag
import io.github.droidkaigi.confsched.eventmap.component.EventMapTabTestTagPrefix
import io.github.droidkaigi.confsched.ui.Inject

class EventMapScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    eventMapServerRobot: DefaultEventMapServerRobot,
) : ScreenRobot by screenRobot,
    EventMapServerRobot by eventMapServerRobot {
    enum class FloorLevel(
        val floorName: String,
    ) {
        Basement("B1F"),
        Ground("1F"),
    }

    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                EventMapScreen(
                    onEventMapItemClick = { },
                )
            }
        }
        waitUntilIdle()
    }

    fun scrollLazyColumnByIndex(
        index: Int,
    ) {
        composeTestRule
            .onNodeWithTag(EventMapLazyColumnTestTag)
            .performScrollToIndex(index)
    }

    fun clickEventMapTab(
        floorLevel: FloorLevel,
    ) {
        composeTestRule
            .onNode(hasTestTag(EventMapTabTestTagPrefix.plus(floorLevel.floorName)))
            .performClick()
        waitUntilIdle()
    }

    fun checkEventMapItemByRoomName(
        roomName: String,
    ) {
        composeTestRule
            .onAllNodes(hasTestTag(EventMapItemTestTag.plus(roomName)))
            .onFirst()
            .assertExists()
    }

    fun checkEventMap(
        floorLevel: FloorLevel,
    ) {
        composeTestRule
            .onNode(hasTestTag(EventMapTabImageTestTag))
            .assertContentDescriptionEquals("Map of ${floorLevel.floorName}")
    }
}
