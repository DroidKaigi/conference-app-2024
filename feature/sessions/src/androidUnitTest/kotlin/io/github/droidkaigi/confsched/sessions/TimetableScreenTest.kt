package io.github.droidkaigi.confsched.sessions

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.TimetableScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import io.github.droidkaigi.confsched.testing.todoChecks
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TimetableScreenTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var timetableScreenRobot: TimetableScreenRobot

    @Test
    fun checkLaunchShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    fun checkLaunchServerErrorShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableServer(ServerStatus.Error)
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    fun checkLaunch() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            checkTimetableItemsDisplayed()
        }
    }

    @Test
    fun checkLaunchAccessibilityShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            checkAccessibilityCapture()
        }
    }

    @Test
    fun checkBookmarkToggleShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            clickFirstSessionBookmark()
            captureScreenWithChecks()
            clickFirstSessionBookmark()
            captureScreenWithChecks()
        }
    }

    @Test
    fun checkScrollShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            scrollTimetable()
            captureScreenWithChecks()
        }
    }

    @Test
    fun checkGridShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            clickTimetableUiTypeChangeButton()
            captureScreenWithChecks()
        }
    }

    @Test
    fun checkGridScrollShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            clickTimetableUiTypeChangeButton()
            scrollTimetable()
            captureScreenWithChecks()
        }
    }

    @Test
    @Config(fontScale = 0.5f)
    fun checkSmallFontScaleShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Config(fontScale = 1.5f)
    fun checkLargeFontScaleShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Config(fontScale = 2.0f)
    fun checkHugeFontScaleShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }
}
