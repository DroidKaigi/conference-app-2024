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
                    run {
                        setupTimetableServer(ServerStatus.Operational)
                        setupFavoriteSession()
                        setupFavoritesScreenContent()
                    }
                    itShould("display favorite session") {
                        captureScreenWithChecks(
                            checks = { checkTimetableListItemsDisplayed() },
                        )
                    }
                    describe("click first session bookmark") {
                        run {
                            clickFirstSessionBookmark()
                        }
                        itShould("display empty view") {
                            captureScreenWithChecks(
                                checks = { checkEmptyViewDisplayed() },
                            )
                        }
                    }
                }

                describe("when server is down") {
                    run {
                        setupTimetableServer(ServerStatus.Error)
                    }
                    describe("when launch") {
                        run {
                            setupFavoritesScreenContent()
                        }
                        itShould("show error message") {
                            captureScreenWithChecks(
                                checks = { checkErrorSnackbarDisplayed() },
                            )
                        }
                    }
                }
            }
        }
    }
}
