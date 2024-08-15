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
                describe("when launch") {
                    run {
                        setupContributorServer(ContributorsServerRobot.ServerStatus.Operational)
                        setupScreenContent()
                    }
                    itShould("show contributors list") {
                        captureScreenWithChecks(
                            checks = { checkContributorsDisplayed() },
                        )
                    }
                }
                describe("when launch with error") {
                    run {
                        setupContributorServer(ContributorsServerRobot.ServerStatus.Error)
                        setupScreenContent()
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
