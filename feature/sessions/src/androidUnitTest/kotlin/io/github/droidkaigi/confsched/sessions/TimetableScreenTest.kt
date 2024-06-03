package io.github.droidkaigi.confsched.sessions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.category.ScreenshotTests
import io.github.droidkaigi.confsched.testing.robot.TimetableScreenRobot
import io.github.droidkaigi.confsched.testing.robot.TimetableScreenRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.todoChecks
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
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
        timetableScreenRobot {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkLaunchServerErrorShot() {
        timetableScreenRobot {
            setupServer(ServerStatus.Error)
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    fun checkLaunch() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            checkTimetableItemsDisplayed()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkLaunchAccessibilityShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            checkAccessibilityCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkBookmarkToggleShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            clickFirstSessionBookmark()
            checkTimetableListCapture()
            clickFirstSessionBookmark()
            checkTimetableListCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkScrollShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            scrollTimetable()
            checkTimetableListCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkGridShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            clickTimetableUiTypeChangeButton()
            checkTimetableListCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkGridScrollShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            clickTimetableUiTypeChangeButton()
            scrollTimetable()
            checkTimetableListCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 0.5f)
    fun checkSmallFontScaleShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 1.5f)
    fun checkLargeFontScaleShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 2.0f)
    fun checkHugeFontScaleShot() {
        timetableScreenRobot {
            setupTimetableScreenContent()
            captureScreenWithChecks(checks = todoChecks("TODO: Please add some checks!"))
        }
    }
}
