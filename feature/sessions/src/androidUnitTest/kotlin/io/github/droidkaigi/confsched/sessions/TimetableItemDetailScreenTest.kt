package io.github.droidkaigi.confsched.sessions

import android.os.Bundle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.TimetableItemDetailScreenRobot
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class TimetableItemDetailScreenTest(private val testCase: DescribedBehavior<TimetableItemDetailScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(
        testInstance = this,
        bundle = Bundle().apply {
            putString(
                timetableItemDetailScreenRouteItemIdParameterName,
                TimetableItemDetailScreenRobot.defaultSessionId,
            )
        },
    )

    @Inject
    lateinit var timetableItemDetailScreenRobot: TimetableItemDetailScreenRobot

    @Test
    fun runTest() {
        runRobot(timetableItemDetailScreenRobot) {
            testCase.execute(timetableItemDetailScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<TimetableItemDetailScreenRobot>> {
            return describeBehaviors<TimetableItemDetailScreenRobot>(name = "TimetableItemDetailScreen") {
                describe("when server is operational") {
                    doIt {
                        setupTimetableServer(ServerStatus.Operational)
                    }
                    describe("when launch") {
                        doIt {
                            setupScreenContent()
                        }
                        itShould("show session detail title") {
                            captureScreenWithChecks(
                                checks = {
                                    checkSessionDetailTitle()
                                },
                            )
                        }
                        itShould("be appropriately accessible") {
                            checkAccessibilityCapture()
                        }
                        describe("click bookmark") {
                            doIt {
                                clickBookmarkButton()
                            }
                            itShould("show bookmarked session") {
                                captureScreenWithChecks(
                                    checks = {
                                        checkBookmarkedSession()
                                    },
                                )
                            }
                            describe("click bookmark again") {
                                doIt {
                                    clickBookmarkButton()
                                }
                                itShould("show unBookmarked session") {
                                    wait5Seconds()
                                    captureScreenWithChecks(
                                        checks = {
                                            checkUnBookmarkSession()
                                        },
                                    )
                                }
                            }
                        }
                        describe("scroll") {
                            doIt {
                                scroll()
                            }
                            itShould("show scrolled session detail") {
                                captureScreenWithChecks(
                                    checks = {
                                        checkTargetAudience()
                                    },
                                )
                            }
                        }
                    }
                    describe("when the description is lengthy") {
                        doIt {
                            setupScreenContentWithLongDescription()
                            scrollToMiddleOfScreen()
                            waitUntilIdle()
                        }
                        itShould("display a more button") {
                            captureScreenWithChecks {
                                checkDisplayingMoreButton()
                            }
                        }
                    }
                    describe("when font scale is small") {
                        doIt {
                            setFontScale(0.5f)
                            setupScreenContent()
                        }
                        itShould("show small font session detail") {
                            captureScreenWithChecks(
                                checks = {
                                    checkSummaryCardTexts()
                                },
                            )
                        }
                    }
                    describe("when font scale is large") {
                        doIt {
                            setFontScale(1.5f)
                            setupScreenContent()
                            scrollLazyColumnByIndex(1)
                        }
                        itShould("show large font session detail") {
                            captureScreenWithChecks(
                                checks = {
                                    checkSummaryCardTexts()
                                },
                            )
                        }
                    }
                    describe("when font scale is huge") {
                        doIt {
                            setFontScale(2.0f)
                            setupScreenContent()
                            scrollLazyColumnByIndex(1)
                        }
                        itShould("show huge font session detail") {
                            captureScreenWithChecks(
                                checks = {
                                    checkSummaryCardTexts()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
