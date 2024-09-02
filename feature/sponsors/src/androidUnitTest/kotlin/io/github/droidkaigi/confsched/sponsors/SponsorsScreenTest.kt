package io.github.droidkaigi.confsched.sponsors

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.SponsorsScreenRobot
import io.github.droidkaigi.confsched.testing.robot.SponsorsServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class SponsorsScreenTest(
    private val testCase: DescribedBehavior<SponsorsScreenRobot>,
) {
    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var sponsorsScreenRobot: SponsorsScreenRobot

    @Test
    fun runTest() {
        runRobot(sponsorsScreenRobot) {
            testCase.execute(sponsorsScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<SponsorsScreenRobot>> {
            return describeBehaviors<SponsorsScreenRobot>(name = "SponsorsScreen") {
                describe("when server is operational") {
                    doIt {
                        setupSponsorsServer(ServerStatus.Operational)
                    }
                    describe("when launch") {
                        doIt {
                            setupScreenContent()
                        }
                        itShould("display platinum sponsors") {
                            captureScreenWithChecks {
                                checkSponsorItemsDisplayed()
                                checkDisplayPlatinumSponsors()
                            }
                        }

                        describe("when scroll to gold sponsors header") {
                            doIt {
                                scrollToGoldSponsorsHeader()
                            }
                            itShould("display gold sponsors") {
                                captureScreenWithChecks {
                                    checkSponsorItemsDisplayed()
                                    checkDisplayGoldSponsors()
                                }
                            }
                        }

                        describe("when scroll to scroll Bottom") {
                            doIt {
                                scrollBottom()
                            }
                            itShould("display supporters sponsors") {
                                captureScreenWithChecks {
                                    checkSponsorItemsDisplayed()
                                    checkDisplaySupportersSponsors()
                                }
                            }
                        }
                    }
                }

                describe("when server is down") {
                    doIt {
                        setupSponsorsServer(ServerStatus.Error)
                    }
                    describe("when launch") {
                        doIt {
                            setupScreenContent()
                        }
                        itShould("does not show sponsors and show snackbar") {
                            captureScreenWithChecks {
                                checkDoesNotSponsorItemsDisplayed()
                                checkErrorSnackbarDisplayed()
                            }
                        }
                    }
                }
            }
        }
    }
}
