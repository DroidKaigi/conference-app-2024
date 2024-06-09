package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import io.github.droidkaigi.confsched.main.MainScreenTab
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.ScreenRobot
import kotlinx.coroutines.test.TestDispatcher
import javax.inject.Inject

class KaigiAppRobot @Inject constructor(
    robotTestRule: RobotTestRule,
    private val defaultScreenRobot: DefaultScreenRobot,
): ScreenRobot by defaultScreenRobot {
    @Inject lateinit var timetableScreenRobot: TimetableScreenRobot

    fun goToAbout() {
        composeTestRule
            .onNode(hasTestTag(MainScreenTab.About.testTag))
            .performClick()
        waitUntilIdle()
    }

    fun goToFloorMap() {
        composeTestRule
            .onNode(hasTestTag(MainScreenTab.FloorMap.testTag))
            .performClick()
        waitUntilIdle()
    }

    fun goToAchievements() {
        composeTestRule
            .onAllNodes(
                matcher = hasTestTag(MainScreenTab.Achievements.testTag),
                useUnmergedTree = true,
            )
            .onFirst()
            .performClick()
        waitUntilIdle()
    }
}
