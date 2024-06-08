package io.github.droidkaigi.confsched.testing

import androidx.compose.ui.test.junit4.ComposeTestRule
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.testing.coroutines.runTestWithLogging
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

inline fun <reified T : ScreenRobot> runRobot(robot: T, noinline block: suspend T.() -> Unit) {
    robot.run(robot, block)
}

class DefaultScreenRobot(override val robotTestRule: RobotTestRule) :
    ScreenRobot,
    ComposeScreenRobot by DefaultComposeScreenRobot(robotTestRule),
    CaptureScreenRobot by DefaultCaptureScreenRobot(robotTestRule)

interface ScreenRobot : ComposeScreenRobot, CaptureScreenRobot {
    val robotTestRule: RobotTestRule

    fun <T : ScreenRobot> run(thiz: T, block: suspend T.() -> Unit) {
        runTestWithLogging(timeout = 30.seconds) {
            thiz.block()
        }
    }
}

class DefaultComposeScreenRobot(private val robotTestRule: RobotTestRule) : ComposeScreenRobot {
    override val composeTestRule: ComposeTestRule
        get() = robotTestRule.composeTestRule
}

interface ComposeScreenRobot {
    val composeTestRule: ComposeTestRule
}

interface CaptureScreenRobot {
    fun captureScreenWithChecks(checks: () -> Unit)

    @Deprecated(
        message = "Use captureScreenWithChecks instead and add checks to ensure screen contents are correct",
        replaceWith = ReplaceWith("captureScreenWithChecks(checks)")
    )
    fun captureScreenWithChecks()
}

fun todoChecks(@Suppress("UNUSED_PARAMETER") reason: String): () -> Unit {
    return {}
}

class DefaultCaptureScreenRobot(private val robotTestRule: RobotTestRule) : CaptureScreenRobot {
    override fun captureScreenWithChecks(checks: () -> Unit) {
        robotTestRule.captureScreen()
        checks()
    }

    @Deprecated(
        "Use captureScreenWithChecks instead and add checks to ensure screen contents are correct",
        replaceWith = ReplaceWith("captureScreenWithChecks(checks)")
    )
    override fun captureScreenWithChecks() {
        robotTestRule.captureScreen()
    }
}

interface TimetableServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupTimetableServer(serverStatus: ServerStatus)
}

class DefaultTimetableServerRobot @Inject constructor(sessionsApiClient: SessionsApiClient) :
    TimetableServerRobot {
    private val fakeSessionsApiClient = sessionsApiClient as FakeSessionsApiClient
    override fun setupTimetableServer(serverStatus: TimetableServerRobot.ServerStatus) {
        fakeSessionsApiClient.setup(
            when (serverStatus) {
                TimetableServerRobot.ServerStatus.Operational -> FakeSessionsApiClient.Status.Operational
                TimetableServerRobot.ServerStatus.Error -> FakeSessionsApiClient.Status.Error
            },
        )
    }
}
