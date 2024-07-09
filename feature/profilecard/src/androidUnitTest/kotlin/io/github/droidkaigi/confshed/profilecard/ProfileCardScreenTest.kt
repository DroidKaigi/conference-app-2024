package io.github.droidkaigi.confshed.profilecard

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.robot.ProfileCardScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ProfileCardScreenTest {
    @get:Rule
    @BindValue
    val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var robot: ProfileCardScreenRobot

    @Test
    fun showEditScreen() {
        runRobot(robot) {
            setupScreenContent()
            captureScreenWithChecks {
                checkEditScreenDisplayed()
            }
        }
    }

    @Test
    fun showCardScreen() {
        runRobot(robot) {
            setupScreenContent()
            captureScreenWithChecks {
                // TODO: need fake repository
                // checkCardScreenDisplayed()
            }
        }
    }
}
