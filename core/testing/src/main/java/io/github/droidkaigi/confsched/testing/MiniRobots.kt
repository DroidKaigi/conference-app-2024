package io.github.droidkaigi.confsched.testing

import androidx.compose.ui.test.junit4.ComposeTestRule
import io.github.droidkaigi.confsched.data.contributors.ContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.FakeContributorsApiClient
import io.github.droidkaigi.confsched.data.eventmap.EventMapApiClient
import io.github.droidkaigi.confsched.data.eventmap.FakeEventMapApiClient
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.testing.coroutines.runTestWithLogging
import kotlinx.coroutines.test.TestDispatcher
import org.robolectric.shadows.ShadowLooper
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

inline fun <reified T : ScreenRobot> runRobot(robot: T, noinline block: suspend T.() -> Unit) {
    robot.run(robot, block)
}

class DefaultScreenRobot @Inject constructor(
    override val robotTestRule: RobotTestRule,
    private val composeScreenRobot: DefaultComposeScreenRobot,
    private val captureScreenRobot: DefaultCaptureScreenRobot,
    private val waitRobot: DefaultWaitRobot,
) :
    ScreenRobot,
    ComposeScreenRobot by composeScreenRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot

interface ScreenRobot : ComposeScreenRobot, CaptureScreenRobot, WaitRobot {
    val robotTestRule: RobotTestRule

    fun <T : ScreenRobot> run(thiz: T, block: suspend T.() -> Unit) {
        runTestWithLogging(timeout = 30.seconds) {
            thiz.block()
        }
    }
}

class DefaultComposeScreenRobot @Inject constructor(private val robotTestRule: RobotTestRule) :
    ComposeScreenRobot {
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
        replaceWith = ReplaceWith("captureScreenWithChecks(checks)"),
    )
    fun captureScreenWithChecks()
}

fun todoChecks(@Suppress("UNUSED_PARAMETER") reason: String): () -> Unit {
    return {}
}

class DefaultCaptureScreenRobot @Inject constructor(private val robotTestRule: RobotTestRule) :
    CaptureScreenRobot {
    override fun captureScreenWithChecks(checks: () -> Unit) {
        robotTestRule.captureScreen()
        checks()
    }

    @Deprecated(
        "Use captureScreenWithChecks instead and add checks to ensure screen contents are correct",
        replaceWith = ReplaceWith("captureScreenWithChecks(checks)"),
    )
    override fun captureScreenWithChecks() {
        robotTestRule.captureScreen()
    }
}

interface WaitRobot {
    fun waitUntilIdle()
}

class DefaultWaitRobot @Inject constructor(
    private val robotTestRule: RobotTestRule,
    private val testDispatcher: TestDispatcher,
) : WaitRobot {
    override fun waitUntilIdle() {
        repeat(5) {
            robotTestRule.composeTestRule.waitForIdle()
            testDispatcher.scheduler.advanceUntilIdle()
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        }
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

interface ContributorsServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupContributorServer(serverStatus: ServerStatus)
}

class DefaultContributorsServerRobot @Inject constructor(contributorsApiClient: ContributorsApiClient) :
    ContributorsServerRobot {
    private val fakeContributorsApiClient = contributorsApiClient as FakeContributorsApiClient
    override fun setupContributorServer(serverStatus: ContributorsServerRobot.ServerStatus) {
        fakeContributorsApiClient.setup(
            when (serverStatus) {
                ContributorsServerRobot.ServerStatus.Operational -> FakeContributorsApiClient.Status.Operational
                ContributorsServerRobot.ServerStatus.Error -> FakeContributorsApiClient.Status.Error
            },
        )
    }
}

interface EventMapServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupContributorServer(serverStatus: ServerStatus)
}

class DefaultEventMapServerRobot @Inject constructor(contributorsApiClient: EventMapApiClient) :
    EventMapServerRobot {
    private val fakeEventMapApiClient = contributorsApiClient as FakeEventMapApiClient
    override fun setupContributorServer(serverStatus: EventMapServerRobot.ServerStatus) {
        fakeEventMapApiClient.setup(
            when (serverStatus) {
                EventMapServerRobot.ServerStatus.Operational -> FakeEventMapApiClient.Status.Operational
                EventMapServerRobot.ServerStatus.Error -> FakeEventMapApiClient.Status.Error
            },
        )
    }
}
