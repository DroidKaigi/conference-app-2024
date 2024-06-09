package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
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
import io.github.droidkaigi.confsched.sessions.timetableItemDetailScreenRoute
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.ScreenRobot
import javax.inject.Inject

class TimetableItemDetailScreenRobot @Inject constructor(
    private val screenRobot: DefaultScreenRobot,
) : ScreenRobot by screenRobot {

    suspend fun setupScreenContent() {
        val firstSessionId = SessionsAllResponse.Companion.fake().sessions.first().id
        robotTestRule.setContentWithNavigation(
            startDestination = "timetableItemDetail/${firstSessionId}",
            route = timetableItemDetailScreenRoute,
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

    companion object {
        val defaultSessionId: String =
            SessionsAllResponse.fake().sessions.find { it.sessionType == "NORMAL" }!!.id
    }
}
