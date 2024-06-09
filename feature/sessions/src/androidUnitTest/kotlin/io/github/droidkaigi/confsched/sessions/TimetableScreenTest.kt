package io.github.droidkaigi.confsched.sessions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.category.ScreenshotTests
import io.github.droidkaigi.confsched.testing.robot.TimetableScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import io.github.droidkaigi.confsched.testing.todoChecks
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(
    qualifiers = RobolectricDeviceQualifiers.NexusOne,
)
class TimetableScreenTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var timetableScreenRobot: TimetableScreenRobot

    @Test
    @Category(ScreenshotTests::class)
    fun checkLaunchShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Category(ScreenshotTests::class)
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
    @Category(ScreenshotTests::class)
    fun checkLaunchAccessibilityShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            checkAccessibilityCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
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
    @Category(ScreenshotTests::class)
    fun checkScrollShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            scrollTimetable()
            captureScreenWithChecks()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkGridShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            clickTimetableUiTypeChangeButton()
            captureScreenWithChecks()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkGridScrollShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            clickTimetableUiTypeChangeButton()
            scrollTimetable()
            captureScreenWithChecks()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 0.5f)
    fun checkSmallFontScaleShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 1.5f)
    fun checkLargeFontScaleShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 2.0f)
    fun checkHugeFontScaleShot() {
        runRobot(timetableScreenRobot) {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }
}
