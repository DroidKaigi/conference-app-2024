package io.github.droidkaigi.confsched.staff

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.StaffServerRobot.ServerStatus.Error
import io.github.droidkaigi.confsched.testing.StaffServerRobot.ServerStatus.Operational
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.StaffScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import io.github.droidkaigi.confsched.testing.todoChecks
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class StaffScreenTest(
    private val testCase: DescribedBehavior<StaffScreenRobot>,
) {
    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var staffScreenRobot: StaffScreenRobot

    @Test
    fun runTest() {
        runRobot(staffScreenRobot) {
            testCase.execute(staffScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<StaffScreenRobot>> {
            return describeBehaviors<StaffScreenRobot>(name = "StaffScreen") {
                describe("when server is operational") {
                    run {
                        setupStaffServer(Operational)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("show staff screen") {
                            captureScreenWithChecks(
                                checks = todoChecks("This screen is still empty now. Please add some checks."),
                            )
                        }
                    }
                }

                describe("when server is down") {
                    run {
                        setupStaffServer(Error)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("show snackbar") {
                            captureScreenWithChecks(
                                checks = todoChecks("This screen is still empty now. Please add some checks."),
                            )
                        }
                    }
                }
            }
        }
    }
}
