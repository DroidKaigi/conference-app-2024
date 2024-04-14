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
import io.github.droidkaigi.confsched.testing.RobotTestEnvironment
import io.github.droidkaigi.confsched.testing.coroutines.runTestWithLogging
import kotlinx.coroutines.test.TestDispatcher
import org.robolectric.shadows.ShadowLooper
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class TimetableItemDetailScreenRobot @Inject constructor(
    private val testDispatcher: TestDispatcher,
) {
    @Inject lateinit var robotTestEnvironment: RobotTestEnvironment
    private val composeTestRule
        get() = robotTestEnvironment.composeTestRule

    operator fun invoke(
        block: suspend TimetableItemDetailScreenRobot.() -> Unit,
    ) {
        runTestWithLogging(timeout = 30.seconds) {
            block()
        }
    }

    suspend fun setupScreenContent() {
        robotTestEnvironment.setContent {
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

    fun waitUntilIdle() {
        repeat(5) {
            composeTestRule.waitForIdle()
            testDispatcher.scheduler.advanceUntilIdle()
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        }
    }

    companion object {
        val defaultSessionId: String =
            SessionsAllResponse.fake().sessions.find { it.sessionType == "NORMAL" }!!.id
    }
}
