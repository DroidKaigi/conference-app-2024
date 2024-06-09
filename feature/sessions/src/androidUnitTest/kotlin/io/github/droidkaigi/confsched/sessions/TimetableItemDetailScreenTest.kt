package io.github.droidkaigi.confsched.sessions

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.category.ScreenshotTests
import io.github.droidkaigi.confsched.testing.robot.TimetableItemDetailScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
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
class TimetableItemDetailScreenTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(
        testInstance = this,
        bundle = Bundle().apply {
            putString(
                timetableItemDetailScreenRouteItemIdParameterName,
                TimetableItemDetailScreenRobot.defaultSessionId,
            )
        },
    )

    @Inject
    lateinit var timetableItemDetailScreenRobot: TimetableItemDetailScreenRobot

    @Test
    @Category(ScreenshotTests::class)
    fun checkLaunchShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkLaunchAccessibilityShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkAccessibilityCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkBookmarkToggleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            clickBookmarkButton()
            checkScreenCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkScrollShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            scroll()
            checkScreenCapture()
            scroll()
            checkScreenCapture()
            scroll()
            checkScreenCapture()
            scroll()
            checkScreenCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 0.5f)
    fun checkSmallFontScaleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 1.5f)
    fun checkLargeFontScaleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    @Config(fontScale = 2.0f)
    fun checkHugeFontScaleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }
}
