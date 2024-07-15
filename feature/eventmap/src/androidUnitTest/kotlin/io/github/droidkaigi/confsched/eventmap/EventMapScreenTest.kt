package io.github.droidkaigi.confsched.eventmap

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DefaultEventMapServerRobot
import io.github.droidkaigi.confsched.testing.DefaultScreenRobot
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.EventMapServerRobot
import io.github.droidkaigi.confsched.testing.EventMapServerRobot.ServerStatus.Operational
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
class EventMapScreenTest(val behavior: DescribedBehavior<EventMapScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var eventMapScreenRobot: EventMapScreenRobot

    @Test
    fun test() = runRobot(eventMapScreenRobot) {
        behavior.execute(eventMapScreenRobot)
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<EventMapScreenRobot>> {
            return describeBehaviors<EventMapScreenRobot>(name = "EventMapScreenRobot") {
                describe("when server is operational") {
                    run {
                        setupEventMapServer(Operational)
                        setupScreenContent()
                    }
                    itShould("show event map items") {
                        captureScreenWithChecks(
                            checks = todoChecks("This screen is still empty now. Please add some checks."),
                        )
                    }
                }
                describe("when server is error") {
                    run {
                        setupEventMapServer(EventMapServerRobot.ServerStatus.Error)
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
