package io.github.droidkaigi.confsched.sessions

import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.robot.TimetableItemDetailScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
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
    fun checkLaunchShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }

    @Test
    fun checkLaunchAccessibilityShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkAccessibilityCapture()
        }
    }

    @Test
    fun checkBookmarkToggleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            clickBookmarkButton()
            checkScreenCapture()
        }
    }

    @Test
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
    @Config(fontScale = 0.5f)
    fun checkSmallFontScaleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }

    @Test
    @Config(fontScale = 1.5f)
    fun checkLargeFontScaleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }

    @Test
    @Config(fontScale = 2.0f)
    fun checkHugeFontScaleShot() {
        runRobot(timetableItemDetailScreenRobot) {
            setupScreenContent()
            checkScreenCapture()
        }
    }
}
