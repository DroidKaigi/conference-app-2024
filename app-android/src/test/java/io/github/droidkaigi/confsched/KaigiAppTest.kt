package io.github.droidkaigi.confsched

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.model.DroidKaigi2023Day
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.category.ScreenshotTests
import io.github.droidkaigi.confsched.testing.robot.KaigiAppRobot
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
@Category(
    ScreenshotTests::class,
)
class KaigiAppTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule<MainActivity>(this)

    @Inject lateinit var kaigiAppRobot: KaigiAppRobot

    @Test
    fun checkStartupShot() {
        kaigiAppRobot {
            capture()
        }
    }

    @Test
    @Config(qualifiers = RobolectricDeviceQualifiers.MediumTablet)
    fun checkMediumTabletLaunchShot() {
        kaigiAppRobot {
            capture()
        }
    }

    @Test
    fun checkStartup() {
        kaigiAppRobot {
            timetableScreenRobot {
                checkTimetableItemsDisplayed()
            }
        }
    }

    @Test
    fun checkTimetableTabs() {
        kaigiAppRobot {
            timetableScreenRobot {
                DroidKaigi2023Day.entries.forEach { dayEntry ->
                    clickTimetableTab(dayEntry.day)
                    capture()
                }
            }
        }
    }
}
