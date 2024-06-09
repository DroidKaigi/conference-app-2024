package io.github.droidkaigi.confsched.sessions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.contributors.ContributorsScreen
import io.github.droidkaigi.confsched.testing.ContributorServerRobot
import io.github.droidkaigi.confsched.testing.DefaultContributorServerRobot
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.ScreenRobot
import io.github.droidkaigi.confsched.testing.category.ScreenshotTests
import io.github.droidkaigi.confsched.testing.runRobot
import io.github.droidkaigi.confsched.testing.todoChecks
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(
    qualifiers = RobolectricDeviceQualifiers.NexusOne,
)
class ContributorScreenTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var contributorScreenRobot: ContributorScreenRobot

    @Test
    @Category(ScreenshotTests::class)
    fun checkScreenContent() {
        runRobot(contributorScreenRobot) {
            setupContributorServer(ContributorServerRobot.ServerStatus.Operational)
            setupScreenContent()

            captureScreenWithChecks(
                checks = todoChecks("This screen is still empty now. Please add some checks.")
            )
        }
    }

    @Test
    @Category(ScreenshotTests::class)
    fun checkErrorScreenContent() {
        runRobot(contributorScreenRobot) {
            setupContributorServer(ContributorServerRobot.ServerStatus.Error)
            setupScreenContent()

            captureScreenWithChecks(
                checks = todoChecks("This screen is still empty now. Please add some checks.")
            )
        }
    }
}

class ContributorScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    contributorServerRobot: DefaultContributorServerRobot,
) : ScreenRobot by screenRobot,
    ContributorServerRobot by contributorServerRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            ContributorsScreen(
                onNavigationIconClick = { },
                onContributorItemClick = { },
            )
        }
    }
}
