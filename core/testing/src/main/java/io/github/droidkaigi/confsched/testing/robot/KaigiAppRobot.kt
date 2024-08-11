package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import io.github.droidkaigi.confsched.main.MainScreenTab
import javax.inject.Inject

class KaigiAppRobot @Inject constructor(
    private val defaultScreenRobot: DefaultScreenRobot,
) : ScreenRobot by defaultScreenRobot {
    @Inject lateinit var timetableScreenRobot: TimetableScreenRobot

    fun goToAbout() {
        composeTestRule
            .onNode(hasTestTag(MainScreenTab.About.testTag))
            .performClick()
        waitUntilIdle()
    }

    fun goToFloorMap() {
        composeTestRule
            .onNode(hasTestTag(MainScreenTab.About.testTag))
            .performClick()
        waitUntilIdle()
    }
}
