package io.github.droidkaigi.confsched.profilecard

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.ProfileCardDataStoreRobot.ProfileCardInputStatus
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
                describe("when profile card is does not exists") {
                    doIt {
                        setupSavedProfileCard(ProfileCardInputStatus.AllNotEntered)
                        setupScreenContent()
                    }
                    itShould("show edit screen") {
                        captureScreenWithChecks {
                            checkEditScreenDisplayed()
                        }
                    }
                    // FIXME Add a test to confirm that it is possible to transition to the Card screen after entering the required input fields, including images.
                    // FIXME Currently, the test code does not allow the user to select and input an image from the Add Image button.
                }
                describe("when profile card is exists") {
                    doIt {
                        setupSavedProfileCard(ProfileCardInputStatus.NoInputOtherThanImage)
                        setupScreenContent()
                    }
                    itShould("show card screen") {
                        captureScreenWithChecks {
                            checkShareProfileCardButtonEnabled()
                            checkCardScreenDisplayed()
                            checkProfileCardFrontDisplayed()
                        }
                    }
                    describe("flip prifle card") {
                        doIt {
                            flipProfileCard()
                        }
                        itShould("back side of the profile card is displayed") {
                            captureScreenWithChecks {
                                checkProfileCardBackDisplayed()
                            }
                        }
                    }
                    describe("when click edit button") {
                        doIt {
                            clickEditButton()
                        }
                        itShould("show edit screen") {
                            captureScreenWithChecks {
                                checkEditScreenDisplayed()
                            }
                        }
                        describe("when if a required field has not been filled in") {
                            doIt {
                                scrollToTestTag(ProfileCardCreateButtonTestTag)
                            }
                            itShould("make sure the Create button is deactivated") {
                                captureScreenWithChecks {
                                    checkCreateButtonDisabled()
                                }
                            }
                        }
                        describe("if all required fields are filled in") {
                            val nickname = "test"
                            val occupation = "test"
                            val link = "test"
                            doIt {
                                inputNickName(nickname)
                                inputOccupation(occupation)
                                inputLink(link)
                                scrollToTestTag(ProfileCardCreateButtonTestTag)
                            }
                            itShould("make sure the Create button is activated") {
                                captureScreenWithChecks {
                                    checkNickName(nickname)
                                    checkOccupation(occupation)
                                    checkLink(link)
                                    checkCreateButtonEnabled()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
