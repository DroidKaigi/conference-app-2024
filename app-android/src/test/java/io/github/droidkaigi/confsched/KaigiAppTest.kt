package io.github.droidkaigi.confsched

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.robot.KaigiAppRobot
import io.github.droidkaigi.confsched.testing.runRobot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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
