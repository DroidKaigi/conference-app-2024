package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.github.takahirom.roborazzi.Dump
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sessions.TimetableListItemBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.TimetableListItemTestTag
import io.github.droidkaigi.confsched.sessions.TimetableScreen
import io.github.droidkaigi.confsched.sessions.TimetableScreenTestTag
import io.github.droidkaigi.confsched.sessions.TimetableUiTypeChangeButtonTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableTabTestTag
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.DefaultTimetableServerRobot
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.ScreenRobot
import io.github.droidkaigi.confsched.testing.TimetableServerRobot
import io.github.droidkaigi.confsched.ui.compositionlocal.FakeClock
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalClock
import javax.inject.Inject

class TimetableScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val defaultTimetableScreenRobot: DefaultTimetableServerRobot
) : ScreenRobot by screenRobot,
    TimetableServerRobot by defaultTimetableScreenRobot{
    fun setupTimetableScreenContent() {
        robotTestRule.setContent {
            CompositionLocalProvider(LocalClock provides FakeClock) {
                KaigiTheme {
                    TimetableScreen(
                        onTimetableItemClick = { },
                    )
                }
            }
        }
        waitUntilIdle()
    }

    fun clickFirstSession() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableListItemTestTag))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun clickFirstSessionBookmark() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableListItemBookmarkIconTestTag))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun clickTimetableUiTypeChangeButton() {
        composeTestRule
            .onNode(hasTestTag(TimetableUiTypeChangeButtonTestTag))
            .performClick()
    }

    fun clickTimetableTab(
        day: Int,
    ) {
        composeTestRule
            .onNode(hasTestTag(TimetableTabTestTag.plus(day)))
            .performClick()
        waitUntilIdle()
    }

    fun scrollTimetable() {
        composeTestRule
            .onNode(hasTestTag(TimetableScreenTestTag))
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 4F / 5,
                    endY = visibleSize.height / 2F,
                )
            }
    }

    fun checkTimetableItemsDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableListItemTestTag))
            .onFirst()
            .assertIsDisplayed()
    }

    fun checkTimetableListCapture() {
        composeTestRule
            .onNode(hasTestTag(TimetableScreenTestTag))
            .captureRoboImage()
    }

    fun checkAccessibilityCapture() {
        composeTestRule
            .onRoot()
            .captureRoboImage(
                roborazziOptions = RoborazziOptions(
                    captureType = RoborazziOptions.CaptureType.Dump(
                        explanation = Dump.AccessibilityExplanation,
                    ),
                ),
            )
    }
}
