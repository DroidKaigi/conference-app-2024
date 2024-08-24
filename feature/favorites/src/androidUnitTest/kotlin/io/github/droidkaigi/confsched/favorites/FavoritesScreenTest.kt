package io.github.droidkaigi.confsched.favorites

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.FavoritesScreenRobot
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
class FavoritesScreenTest(
    private val testCase: DescribedBehavior<FavoritesScreenRobot>,
) {
    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var favoritesScreenRobot: FavoritesScreenRobot

    @Test
    fun runTest() {
        runRobot(robot = favoritesScreenRobot) {
            testCase.execute(favoritesScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<FavoritesScreenRobot>> {
            return describeBehaviors<FavoritesScreenRobot>(name = "FavoritesScreen") {
                describe("when server is operational") {
                    doIt {
                        setupTimetableServer(ServerStatus.Operational)
                    }
                    describe("setup single favorite session") {
                        doIt {
                            setupSingleFavoriteSession()
                            setupFavoritesScreenContent()
                        }
                        itShould("display favorite session") {
                            captureScreenWithChecks(
                                checks = { checkTimetableListItemsDisplayed() },
                            )
                        }
                        describe("click first session bookmark") {
                            doIt {
                                clickFirstSessionBookmark()
                            }
                            itShould("display empty view") {
                                captureScreenWithChecks(
                                    checks = { checkEmptyViewDisplayed() },
                                )
                            }
                        }
                    }
                    describe("setup many favorite sessions") {
                        doIt {
                            setupFavoriteSessions()
                            setupFavoritesScreenContent()
                        }
                        itShould("display favorite session") {
                            captureScreenWithChecks(
                                checks = { checkTimetableListItemsDisplayed() },
                            )
                        }
                        describe("click first session bookmark") {
                            doIt {
                                clickFirstSessionBookmark()
                            }
                            itShould("display favorite session without first session") {
                                captureScreenWithChecks(
                                    checks = { checkTimetableListItemsDisplayed() },
                                )
                            }
                        }
                        describe("scroll favorites") {
                            doIt {
                                scrollFavorites()
                            }
                            itShould("first session is not displayed") {
                                captureScreenWithChecks(
                                    checks = { checkTimetableListFirstItemNotDisplayed() },
                                )
                            }
                        }
                    }
                }
                describe("when server is down") {
                    doIt {
                        setupTimetableServer(ServerStatus.Error)
                    }
                    describe("when launch") {
                        doIt {
                            setupFavoritesScreenContent()
                        }
                        itShould("show error message") {
                            captureScreenWithChecks(
                                checks = { checkErrorSnackbarDisplayed() },
                            )
                        }
                    }
                }

                describe("when device is tablet") {
                    doIt {
                        setupTabletDevice()
                        setupTimetableServer(ServerStatus.Operational)
                        setupFavoriteSessions()
                        setupFavoritesScreenContent()
                    }
                    itShould("show timetable items") {
                        captureScreenWithChecks(
                            checks = { checkTimetableListItemsDisplayed() },
                        )
                    }
                }
            }
        }
    }
}
