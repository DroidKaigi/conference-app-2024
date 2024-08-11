package io.github.droidkaigi.confsched.sponsors

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.SponsorsScreenRobot
import io.github.droidkaigi.confsched.testing.robot.SponsorsServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.robot.todoChecks
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
                    run {
                        setupSponsorsServer(ServerStatus.Operational)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("display sponsors") {
                            captureScreenWithChecks(
                                checks = todoChecks("This screen is still empty now. Please add some checks."),
                            )
                        }
                    }
                }

                describe("when server is down") {
                    run {
                        setupSponsorsServer(ServerStatus.Error)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("show error message") {
                            captureScreenWithChecks(
                                checks = todoChecks("This screen is still empty now. Please add some checks."),
                            )
                        }
                    }
                }
            }
        }
    }
}
