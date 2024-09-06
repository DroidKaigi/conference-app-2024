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
                    doIt {
                        setupContributorServer(ContributorsServerRobot.ServerStatus.Operational)
                    }
                    describe("when launch") {
                        doIt {
                            setupScreenContent()
                        }
                        itShould("show first and second contributors") {
                            captureScreenWithChecks {
                                checkShowFirstAndSecondContributors()
                            }
                        }
                        itShould("show contributors total count") {
                            captureScreenWithChecks {
                                checkContributorTotalCountDisplayed()
                            }
                        }

                        describe("when scroll to index 10") {
                            doIt {
                                scrollToIndex10()
                            }
                            itShould("show contributors") {
                                captureScreenWithChecks {
                                    checkContributorItemsDisplayed()
                                }
                            }
                        }
                    }

                    describe("when server is down") {
                        doIt {
                            setupContributorServer(ContributorsServerRobot.ServerStatus.Error)
                        }
                        describe("when launch") {
                            doIt {
                                setupScreenContent()
                            }
                            itShould("does not show contributor and show snackbar") {
                                captureScreenWithChecks(
                                    checks = {
                                        checkDoesNotFirstContributorItemDisplayed()
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
