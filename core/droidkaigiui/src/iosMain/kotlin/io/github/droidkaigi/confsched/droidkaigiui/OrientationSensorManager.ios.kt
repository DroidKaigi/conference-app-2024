package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue

@Composable
internal actual fun getOrientationSensorManager(
    onOrientationChanged: (Orientation) -> Unit,
): OrientationSensorManager {
    return remember(onOrientationChanged) {
        IosOrientationSensorManager(
            onOrientationChanged = onOrientationChanged,
        )
    }
}

internal class IosOrientationSensorManager(
    override val onOrientationChanged: (Orientation) -> Unit,
) : OrientationSensorManager {
    private val motionManager = CMMotionManager()

    override fun start() {
        if (!motionManager.deviceMotionAvailable) {
            return
        }
        NSOperationQueue.mainQueue().let { queue ->
            motionManager.startDeviceMotionUpdatesToQueue(
                queue,
            ) { motion, _ ->
                if (motion == null) {
                    return@startDeviceMotionUpdatesToQueue
                }
                onOrientationChanged(
                    Orientation(
                        azimuth = motion.attitude.yaw.toFloat(),
                        // Unlike Android, iOS uses a left-handed coordinate system, so we need to invert the pitch value.
                        pitch = -motion.attitude.pitch.toFloat(),
                        roll = motion.attitude.roll.toFloat(),
                    ),
                )
            }
        }
    }

    override fun stop() {
        if (!motionManager.deviceMotionActive) {
            return
        }
        motionManager.stopDeviceMotionUpdates()
    }
}
