package io.github.droidkaigi.confsched.contributors

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.ContributorsScreenRobot
import io.github.droidkaigi.confsched.testing.robot.ContributorsServerRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class ContributorsScreenTest(private val testCase: DescribedBehavior<ContributorsScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var contributorsScreenRobot: ContributorsScreenRobot

    @Test
    fun runTest() {
        runRobot(contributorsScreenRobot) {
            testCase.execute(contributorsScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<ContributorsScreenRobot>> {
            return describeBehaviors<ContributorsScreenRobot>(name = "ContributorsScreen") {
                describe("when server is operational") {
                    run {
                        setupContributorServer(ContributorsServerRobot.ServerStatus.Operational)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("show contributor 1 to 5") {
                            captureScreenWithChecks {
                                checkExistsContributorItem(
                                    fromTo = 0 to 5,
                                )
                            }
                        }
                        describe("when scroll to contributor 9") {
                            run {
                                scrollToTestTag(ContributorsItemTestTag.plus(9))
                            }
                            itShould("show contributor 6 to 10") {
                                captureScreenWithChecks {
                                    checkExistsContributorItem(
                                        fromTo = 5 to 10,
                                    )
                                }
                            }
                        }
                        describe("when scroll to contributor 12") {
                            run {
                                scrollToTestTag(ContributorsItemTestTag.plus(12))
                            }
                            itShould("show contributor 11 to 15") {
                                captureScreenWithChecks {
                                    checkExistsContributorItem(
                                        fromTo = 10 to 15,
                                    )
                                }
                            }
                        }
                        describe("when scroll to contributor 20") {
                            run {
                                scrollToTestTag(ContributorsItemTestTag.plus(20))
                            }
                            itShould("show contributor 16 to 20") {
                                captureScreenWithChecks {
                                    checkExistsContributorItem(
                                        fromTo = 15 to 20,
                                    )
                                }
                            }
                        }
                    }

                    describe("when server is down") {
                        run {
                            setupContributorServer(ContributorsServerRobot.ServerStatus.Error)
                        }
                        describe("when launch") {
                            run {
                                setupScreenContent()
                            }
                            itShould("does not show contributor, and show snackbar") {
                                captureScreenWithChecks(
                                    checks = {
                                        checkDoesNotExistsContributorItem()
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
