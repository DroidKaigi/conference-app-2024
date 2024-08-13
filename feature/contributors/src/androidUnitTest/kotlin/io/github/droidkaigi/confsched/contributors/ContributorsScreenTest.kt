package io.github.droidkaigi.confsched.contributors

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.ContributorsServerRobot
import io.github.droidkaigi.confsched.testing.robot.DefaultContributorsServerRobot
import io.github.droidkaigi.confsched.testing.robot.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.robot.ScreenRobot
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

    fun checkContributorsDisplayed() {
        composeTestRule
            .onNode(hasTestTag(ContributorsScreenTestTag))
            .assertIsDisplayed()
    }

    fun checkErrorSnackbarDisplayed() {
        composeTestRule
            .onNode(
                hasText("Fake IO Exception"),
                useUnmergedTree = true,
            ).assertIsDisplayed()
    }
}
