package io.github.droidkaigi.confsched.staff

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.StaffScreenRobot
import io.github.droidkaigi.confsched.testing.robot.StaffServerRobot.ServerStatus.Error
import io.github.droidkaigi.confsched.testing.robot.StaffServerRobot.ServerStatus.Operational
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
                        setupStaffServer(Operational)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("show staff 1 to 5") {
                            captureScreenWithChecks {
                                checkExistsStaffItem(
                                    fromTo = 0 to 5,
                                )
                            }
                        }
                        describe("when scroll to staff 9") {
                            run {
                                scrollToTestTag(StaffItemTestTag.plus(9))
                            }
                            itShould("show staff 6 to 10") {
                                captureScreenWithChecks {
                                    checkExistsStaffItem(
                                        fromTo = 5 to 10,
                                    )
                                }
                            }
                        }
                        describe("when scroll to staff 12") {
                            run {
                                scrollToTestTag(StaffItemTestTag.plus(12))
                            }
                            itShould("show staff 11 to 15") {
                                captureScreenWithChecks {
                                    checkExistsStaffItem(
                                        fromTo = 10 to 15,
                                    )
                                }
                            }
                        }
                        describe("when scroll to staff 20") {
                            run {
                                scrollToTestTag(StaffItemTestTag.plus(20))
                            }
                            itShould("show staff 16 to 20") {
                                captureScreenWithChecks {
                                    checkExistsStaffItem(
                                        fromTo = 15 to 20,
                                    )
                                }
                            }
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
                        itShould("does not show staff, and show snackbar") {
                            captureScreenWithChecks(
                                checks = {
                                    checkDoesNotExistsStaffItem()
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
