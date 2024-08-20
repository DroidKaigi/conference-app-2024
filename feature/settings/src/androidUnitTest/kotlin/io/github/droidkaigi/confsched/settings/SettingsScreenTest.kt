package io.github.droidkaigi.confsched.settings

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.SettingsDataStoreRobot
import io.github.droidkaigi.confsched.testing.robot.SettingsScreenRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
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
                describe("when launch use font dot gothic") {
                    doIt {
                        setupSettings(SettingsDataStoreRobot.SettingsStatus.UseDotGothic16FontFamily)
                        setupScreenContent()
                    }
                    itShould("show settings contents") {
                        captureScreenWithChecks {
                            checkEnsureThatDotGothicFontIsUsed()
                        }
                    }
                    describe("when click use font item") {
                        doIt {
                            clickUseFontItem()
                        }
                        itShould("show available fonts") {
                            captureScreenWithChecks {
                                checkAllDisplayedAvailableFont()
                            }
                        }
                        describe("when click system default font") {
                            doIt {
                                clickSystemDefaultFontItem()
                            }
                            itShould("selected system default font") {
                                captureScreenWithChecks {
                                    checkEnsureThatSystemDefaultFontIsUsed()
                                }
                            }
                        }
                        describe("when click dot gothic font") {
                            doIt {
                                clickDotGothicFontItem()
                            }
                            itShould("selected dot gothic font") {
                                captureScreenWithChecks {
                                    checkEnsureThatDotGothicFontIsUsed()
                                }
                            }
                        }
                    }
                }
                describe("when launch use font default system font") {
                    doIt {
                        setupSettings(SettingsDataStoreRobot.SettingsStatus.UseSystemDefaultFont)
                        setupScreenContent()
                    }
                    itShould("show settings contents") {
                        captureScreenWithChecks {
                            checkEnsureThatSystemDefaultFontIsUsed()
                        }
                    }
                    describe("when click use font item") {
                        doIt {
                            clickUseFontItem()
                        }
                        itShould("show available fonts") {
                            captureScreenWithChecks {
                                checkAllDisplayedAvailableFont()
                            }
                        }
                        describe("when click dot gothic font") {
                            doIt {
                                clickDotGothicFontItem()
                            }
                            itShould("selected dot gothic font") {
                                captureScreenWithChecks {
                                    checkEnsureThatDotGothicFontIsUsed()
                                }
                            }
                        }
                        describe("when click system default font") {
                            doIt {
                                clickSystemDefaultFontItem()
                            }
                            itShould("selected system default font") {
                                captureScreenWithChecks {
                                    checkEnsureThatSystemDefaultFontIsUsed()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
