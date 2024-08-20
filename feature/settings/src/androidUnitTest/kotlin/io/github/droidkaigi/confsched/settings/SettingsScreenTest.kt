package io.github.droidkaigi.confsched.settings

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.SettingsScreenRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.robot.todoChecks
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class SettingsScreenTest(
    private val testCase: DescribedBehavior<SettingsScreenRobot>,
) {
    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var settingsScreenRobot: SettingsScreenRobot

    @Test
    fun runTest() {
        runRobot(settingsScreenRobot) {
            testCase.execute(settingsScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<SettingsScreenRobot>> {
            return describeBehaviors<SettingsScreenRobot>(name = "SettingsScreen") {
                describe("when launch") {
                    run {
                        setupScreenContent()
                    }
                    itShould("show settings contents") {
                        captureScreenWithChecks {
                            todoChecks("TODO")
                        }
                    }
                }
            }
        }
    }
}
