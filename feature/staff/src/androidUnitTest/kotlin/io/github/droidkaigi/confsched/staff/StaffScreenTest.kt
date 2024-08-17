package io.github.droidkaigi.confsched.staff

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.StaffScreenRobot
import io.github.droidkaigi.confsched.testing.robot.StaffServerRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
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
                        setupStaffServer(StaffServerRobot.ServerStatus.Operational)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("show first and second staffs") {
                            captureScreenWithChecks {
                                checkRangeStaffItemsDisplayed(
                                    fromTo = 0..2,
                                )
                            }
                        }

                        describe("when scroll to index 10") {
                            run {
                                scrollToIndex10()
                            }
                            itShould("show staffs") {
                                captureScreenWithChecks {
                                    checkStaffItemsDisplayed()
                                }
                            }
                        }
                    }

                    describe("when server is down") {
                        run {
                            setupStaffServer(StaffServerRobot.ServerStatus.Error)
                        }
                        describe("when launch") {
                            run {
                                setupScreenContent()
                            }
                            itShould("does not show staff and show snackbar") {
                                captureScreenWithChecks(
                                    checks = {
                                        checkDoesNotFirstStaffItemDisplayed()
                                        checkErrorSnackbarDisplayed()
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
