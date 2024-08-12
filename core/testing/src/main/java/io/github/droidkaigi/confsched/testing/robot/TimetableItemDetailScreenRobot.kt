package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.github.takahirom.roborazzi.Dump
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.droidkaigi.confsched.data.sessions.fake
import io.github.droidkaigi.confsched.data.sessions.response.SessionsAllResponse
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreen
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.sessions.component.TargetAudienceSectionTestTag
import io.github.droidkaigi.confsched.sessions.component.TimetableItemDetailHeadlineTestTag
import io.github.droidkaigi.confsched.sessions.navigation.TimetableItemDetailDestination
import javax.inject.Inject

class TimetableItemDetailScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
    private val timetableServerRobot: DefaultTimetableServerRobot,
    private val fontScaleRobot: DefaultFontScaleRobot,
) : ScreenRobot by screenRobot,
    TimetableServerRobot by timetableServerRobot,
    FontScaleRobot by fontScaleRobot {

    suspend fun setupScreenContent() {
        val firstSessionId = SessionsAllResponse.Companion.fake().sessions.first().id
        robotTestRule.setContentWithNavigation<TimetableItemDetailDestination>(
            startDestination = { TimetableItemDetailDestination(firstSessionId) },
        ) {
            KaigiTheme {
                TimetableItemDetailScreen(
                    onNavigationIconClick = { },
                    onLinkClick = { },
                    onCalendarRegistrationClick = { },
                    onShareClick = { },
                )
            }
        }
        waitUntilIdle()
    }

    suspend fun clickBookmarkButton() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailBookmarkIconTestTag))
            .performClick()
        waitUntilIdle()
    }

    fun scroll() {
        composeTestRule
            .onRoot()
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 3F / 4,
                    endY = visibleSize.height / 2F,
                )
            }
    }

    // TODO https://github.com/DroidKaigi/conference-app-2024/issues/372
    fun scrollLazyColumnByIndex(
        index: Int
    ) {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailScreenLazyColumnTestTag))
            .performScrollToIndex(index)
    }

    fun checkScreenCapture() {
        composeTestRule
            .onNode(isRoot())
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

    fun checkSessionDetailTitle() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailHeadlineTestTag))
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Demo Welcome Talk 1")
    }

    fun checkBookmarkedSession() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailBookmarkIconTestTag))
            .assertContentDescriptionEquals("Bookmarked")
    }

    fun checkUnBookmarkSession() {
        composeTestRule
            .onNode(hasTestTag(TimetableItemDetailBookmarkIconTestTag))
            .assertContentDescriptionEquals("Not Bookmarked")
    }

    fun checkTargetAudience() {
        composeTestRule
            .onNode(hasTestTag(TargetAudienceSectionTestTag))
            .onChildren()
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Target Audience")
    }

    companion object {
        val defaultSessionId: String =
            SessionsAllResponse.fake().sessions.find { it.sessionType == "NORMAL" }!!.id
    }
}
