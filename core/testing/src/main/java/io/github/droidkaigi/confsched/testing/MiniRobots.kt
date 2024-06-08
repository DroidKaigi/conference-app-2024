package io.github.droidkaigi.confsched.testing

class DefaultScreenRobot<Robot>(private val robotTestRule: RobotTestRule) :
    ScreenRobot<Robot>,
    CaptureScreenRobot by DefaultCaptureScreenRobot(robotTestRule)

interface ScreenRobot<Robot> : CaptureScreenRobot {
    operator fun invoke(block: Robot.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        block(this as Robot)
    }
}

interface SetContentRobot {
    fun setContent()
}

interface CaptureScreenRobot {
    fun captureScreenWithChecks(checks: () -> Unit)
}

fun todoChecks(@Suppress("UNUSED_PARAMETER") reason: String): () -> Unit {
    return {}
}

class DefaultCaptureScreenRobot(private val robotTestRule: RobotTestRule) : CaptureScreenRobot {
    override fun captureScreenWithChecks(checks: () -> Unit) {
        robotTestRule.captureScreen()
        checks()
    }
}
