package io.github.droidkaigi.confsched.testing

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
