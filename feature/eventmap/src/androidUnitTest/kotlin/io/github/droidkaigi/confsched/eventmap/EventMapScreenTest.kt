package io.github.droidkaigi.confsched.eventmap

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DefaultEventMapServerRobot
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.EventMapServerRobot
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
class EventMapScreenTest {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var eventMapScreenRobot: EventMapScreenRobot

    @Test
    fun checkScreenContent() {
        runRobot(eventMapScreenRobot) {
            setupContributorServer(EventMapServerRobot.ServerStatus.Operational)
            setupScreenContent()

            captureScreenWithChecks(
                checks = todoChecks("This screen is still empty now. Please add some checks."),
            )
        }
    }

    @Test
    fun checkErrorScreenContent() {
        runRobot(eventMapScreenRobot) {
            setupContributorServer(EventMapServerRobot.ServerStatus.Error)
            setupScreenContent()

            captureScreenWithChecks(
                checks = todoChecks("This screen is still empty now. Please add some checks."),
            )
        }
    }
}

class EventMapScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    eventMapServerRobot: DefaultEventMapServerRobot,
) : ScreenRobot by screenRobot,
    EventMapServerRobot by eventMapServerRobot {
    fun setupScreenContent() {
        robotTestRule.setContent {
            EventMapScreen(
                onNavigationIconClick = { },
                onEventMapItemClick = { },
            )
        }
    }
}
