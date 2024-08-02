package io.github.droidkaigi.confsched.contributors

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.ContributorsServerRobot
import io.github.droidkaigi.confsched.testing.DefaultContributorsServerRobot
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.ScreenRobot
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.runRobot
import io.github.droidkaigi.confsched.testing.todoChecks
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
                            checks = todoChecks("This screen is still empty now. Please add some checks."),
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
                            checks = todoChecks("This screen is still empty now. Please add some checks."),
                        )
                    }
                }
            }
        }
    }
}

class ContributorsScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    contributorsServerRobot: DefaultContributorsServerRobot,
) : ScreenRobot by screenRobot,
    ContributorsServerRobot by contributorsServerRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            ContributorsScreen(
                onNavigationIconClick = { },
                onContributorsItemClick = { },
            )
        }
    }
}
