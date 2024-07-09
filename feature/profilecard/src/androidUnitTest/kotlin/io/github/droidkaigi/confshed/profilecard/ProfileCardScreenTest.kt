package io.github.droidkaigi.confshed.profilecard

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.ProfileCardScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class ProfileCardScreenTest(
    private val testCase: DescribedBehavior<ProfileCardScreenRobot>,
) {
    @get:Rule
    @BindValue
    val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var robot: ProfileCardScreenRobot

    @Test
    fun runTest() {
        runRobot(robot) {
            testCase.execute(robot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<ProfileCardScreenRobot>> {
            return describeBehaviors("ProfileCardScreen") {
                describe("when profile card is not found") {
                    run {
                        setupScreenContent()
                    }
                    itShould("show edit screen") {
                        checkEditScreenDisplayed()
                    }
                }
            }
        }
    }
}
