package io.github.droidkaigi.confsched.profilecard

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.ProfileCardScreenRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
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
                        captureScreenWithChecks {
                            checkEditScreenDisplayed()
                        }
                    }
                    val nickName = "test"
                    val occupation = "test"
                    describe("input nickname") {
                        run {
                            inputNickName(nickName)
                        }
                        itShould("show nickname") {
                            captureScreenWithChecks {
                                checkEditScreenDisplayed()
                            }
                        }
                        describe("input occupation") {
                            run {
                                inputOccupation(occupation)
                            }
                            itShould("show occupation") {
                                captureScreenWithChecks {
                                    checkEditScreenDisplayed()
                                }
                            }
                            describe("click create button") {
                                run {
                                    clickCreateButton()
                                }
                                itShould("show card screen") {
                                    captureScreenWithChecks {
                                        checkCardScreenDisplayed()
                                    }
                                }
                                describe("click edit button") {
                                    run {
                                        clickEditButton()
                                    }
                                    itShould("show edit screen") {
                                        captureScreenWithChecks {
                                            wait5Seconds()
                                            checkEditScreenDisplayed()
                                            checkNickName(nickName)
                                            checkOccupation(occupation)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // FIXME: java.util.concurrent.CancellationException: The test timed out at saveProfileCard
                // describe("when profile card is found") {
                //     run {
                //         val profileCard = ProfileCard(
                //             nickname = "test",
                //             occupation = "test",
                //             link = null,
                //             image = null,
                //             theme = ProfileCardTheme.Iguana
                //         )
                //         saveProfileCard(profileCard)
                //         setupScreenContent()
                //     }
                //     itShould("show card screen") {
                //         captureScreenWithChecks {
                //             checkCardScreenDisplayed()
                //         }
                //     }
                // }
            }
        }
    }
}
