package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.eventmap.EventMapItemTestTag
import io.github.droidkaigi.confsched.eventmap.EventMapLazyColumnTestTag
import io.github.droidkaigi.confsched.eventmap.EventMapScreen
import io.github.droidkaigi.confsched.ui.Inject

class EventMapScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    eventMapServerRobot: DefaultEventMapServerRobot,
) : ScreenRobot by screenRobot,
    EventMapServerRobot by eventMapServerRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                EventMapScreen(
                    onNavigationIconClick = { },
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

    fun checkEventMapItemByRoomName(
        roomName: String,
    ) {
        composeTestRule
            .onAllNodes(hasTestTag(EventMapItemTestTag.plus(roomName)))
            .onFirst()
            .assertExists()
    }
}
