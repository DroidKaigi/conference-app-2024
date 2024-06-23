package io.github.droidkaigi.confsched.contributors

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.ContributorsServerRobot
import io.github.droidkaigi.confsched.testing.DefaultContributorsServerRobot
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.ScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import io.github.droidkaigi.confsched.testing.todoChecks
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ContributorsScreenTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var contributorsScreenRobot: ContributorsScreenRobot

    @Test
    fun checkScreenContent() {
        runRobot(contributorsScreenRobot) {
            setupContributorServer(ContributorsServerRobot.ServerStatus.Operational)
            setupScreenContent()

            captureScreenWithChecks(
                checks = todoChecks("This screen is still empty now. Please add some checks."),
            )
        }
    }

    @Test
    fun checkErrorScreenContent() {
        runRobot(contributorsScreenRobot) {
            setupContributorServer(ContributorsServerRobot.ServerStatus.Error)
            setupScreenContent()

            captureScreenWithChecks(
                checks = todoChecks("This screen is still empty now. Please add some checks."),
            )
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
