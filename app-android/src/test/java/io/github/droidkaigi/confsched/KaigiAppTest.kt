package io.github.droidkaigi.confsched

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.robot.KaigiAppRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class KaigiAppTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(MainActivity::class, this)

    @Inject lateinit var kaigiAppRobot: KaigiAppRobot

    @Test
    fun checkStartupShot() {
        runRobot(kaigiAppRobot) {
            waitUntilIdle()
            captureScreenWithChecks {
                runRobot(timetableScreenRobot) {
                    checkTimetableItemsDisplayed()
                }
            }
        }
    }

    @Test
    @Config(qualifiers = RobolectricDeviceQualifiers.MediumTablet)
    fun checkMediumTabletLaunchShot() {
        runRobot(kaigiAppRobot) {
            waitUntilIdle()
            captureScreenWithChecks()
        }
    }
}
