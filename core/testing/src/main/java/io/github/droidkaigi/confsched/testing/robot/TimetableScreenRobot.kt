package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.github.takahirom.roborazzi.Dump
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sessions.TimetableListItemBookmarkIconTestTag
import io.github.droidkaigi.confsched.sessions.TimetableListItemTestTag
import io.github.droidkaigi.confsched.sessions.TimetableScreen
import io.github.droidkaigi.confsched.sessions.TimetableScreenTestTag
import io.github.droidkaigi.confsched.sessions.TimetableUiTypeChangeButtonTestTag
import io.github.droidkaigi.confsched.sessions.section.TimetableTabTestTag
import io.github.droidkaigi.confsched.testing.RobotTestEnvironment
import io.github.droidkaigi.confsched.testing.coroutines.runTestWithLogging
import io.github.droidkaigi.confsched.ui.compositionlocal.FakeClock
import io.github.droidkaigi.confsched.ui.compositionlocal.LocalClock
import kotlinx.coroutines.test.TestDispatcher
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class TimetableScreenRobot @Inject constructor(
    private val testDispatcher: TestDispatcher,
) {
    @Inject lateinit var robotTestEnvironment: RobotTestEnvironment
    private val composeTestRule
        get() = robotTestEnvironment.composeTestRule

    @Inject lateinit var sessionsApiClient: SessionsApiClient
    val fakeSessionsApiClient: FakeSessionsApiClient
        get() = sessionsApiClient as FakeSessionsApiClient

    operator fun invoke(
        block: TimetableScreenRobot.() -> Unit,
    ) {
        runTestWithLogging(timeout = 30.seconds) {
            block()
        }
    }

    fun setupTimetableScreenContent() {
        robotTestEnvironment.setContent {
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

    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupServer(serverStatus: ServerStatus) {
        fakeSessionsApiClient.setup(
            when (serverStatus) {
                ServerStatus.Operational -> FakeSessionsApiClient.Status.Operational
                ServerStatus.Error -> FakeSessionsApiClient.Status.Error
            },
        )
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

    fun checkScreenCapture() {
        composeTestRule
            .onNode(isRoot())
            .captureRoboImage()
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

    fun waitUntilIdle() {
        composeTestRule.waitForIdle()
        testDispatcher.scheduler.advanceUntilIdle()
    }
}
