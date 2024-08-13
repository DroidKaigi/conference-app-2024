package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.github.takahirom.roborazzi.Dump
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.sessions.TimetableScreen
import io.github.droidkaigi.confsched.sessions.TimetableScreenTestTag
import io.github.droidkaigi.confsched.sessions.TimetableUiTypeChangeButtonTestTag
import io.github.droidkaigi.confsched.sessions.component.TimetableGridItemTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableGridTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableListTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableTabTestTag
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardBookmarkButtonTestTag
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardBookmarkedIconTestTag
import io.github.droidkaigi.confsched.ui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.ui.compositionlocal.FakeClock
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalClock
import javax.inject.Inject

class TimetableScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot {
    val clickedItems = mutableSetOf<TimetableItem>()

    fun setupTimetableScreenContent() {
        robotTestRule.setContent {
            CompositionLocalProvider(LocalClock provides FakeClock) {
                KaigiTheme {
                    TimetableScreen(
                        onTimetableItemClick = {
                            clickedItems.add(it)
                        },
                    )
                }
            }
        }
        waitUntilIdle()
    }

    fun clickFirstSession() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun clickFirstSessionBookmark() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardBookmarkButtonTestTag))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    fun clickTimetableUiTypeChangeButton() {
        composeTestRule
            .onNode(hasTestTag(TimetableUiTypeChangeButtonTestTag))
            .performClick()
        waitUntilIdle()
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
                    endY = visibleSize.height / 5F,
                )
            }
    }

    fun checkClickedItemsExists() {
        assert(clickedItems.isNotEmpty())
    }

    fun checkTimetableListDisplayed() {
        composeTestRule
            .onNode(hasTestTag(TimetableListTestTag))
            .assertIsDisplayed()
    }

    fun checkTimetableListItemsDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertIsDisplayed()
    }

    fun checkTimetableListFirstItemNotDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertIsNotDisplayed()
    }

    fun checkTimetableGridDisplayed() {
        composeTestRule
            .onNode(hasTestTag(TimetableGridTestTag))
            .assertIsDisplayed()
    }

    fun checkTimetableGridItemsDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableGridItemTestTag))
            .onFirst()
            .assertIsDisplayed()
    }

    fun checkTimetableGridFirstItemNotDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableGridItemTestTag))
            .onFirst()
            .assertIsNotDisplayed()
    }

    fun checkFirstSessionBookmarkedIconDisplayed() {
        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardBookmarkButtonTestTag), useUnmergedTree = true)
            .onFirst()
            .assert(hasTestTag(TimetableItemCardBookmarkButtonTestTag))
            .onChild()
            .assert(hasTestTag(TimetableItemCardBookmarkedIconTestTag))
            .assertIsDisplayed()
    }

    fun checkErrorSnackbarDisplayed() {
        composeTestRule
            .onNode(hasText("Fake IO Exception"))
            .isDisplayed()
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
