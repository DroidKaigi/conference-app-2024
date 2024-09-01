package io.github.droidkaigi.confsched.profilecard

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import io.github.droidkaigi.confsched.droidkaigiui.DeviceOrientationScope
import io.github.droidkaigi.confsched.droidkaigiui.Orientation
import kotlin.math.PI
import kotlin.math.roundToInt

/**
 * Extension function to apply tilt effect based on device orientation.
 *
 * @param deviceOrientationScope The scope containing the device orientation information.
 * @return A [Modifier] with the tilt effect applied.
 */
fun Modifier.tiltEffect(deviceOrientationScope: DeviceOrientationScope) =
    this then TiltEffectElement(
        orientation = deviceOrientationScope.orientation,
    )

/**
 * Modifier element to handle tilt effect based on device orientation.
 *
 * @property orientation The current orientation of the device.
 */
private data class TiltEffectElement(
    private val orientation: Orientation,
) : ModifierNodeElement<TiltEffectNode>() {
    override fun create() = TiltEffectNode(orientation)

    override fun update(node: TiltEffectNode) {
        node.orientation = orientation
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "TiltEffectNode"
        properties["orientation"] = orientation
    }
}

/**
 * A node to apply tilt effect to a layout based on device orientation.
 *
 * @property orientation The current orientation of the device.
 * @property previousTiltRoll The last computed X-axis tilt value (roll), used for stabilization.
 * @property previousTiltPitch The last computed Y-axis tilt value (pitch), used for stabilization.
 */
private data class TiltEffectNode(
    var orientation: Orientation,
    var previousTiltRoll: Float = 0f,
    var previousTiltPitch: Float = 0f,
) : Modifier.Node(), LayoutModifierNode {

    /** Maximum tilt angle applied to the element. */
    private val maxTiltAngle = 5f

    /** The minimum and maximum allowable angles for tilt to be considered valid. */
    private val minTiltAngle = -75f
    private val maxTiltAngleRange = 75f

    /** Computes the pitch in degrees from the current orientation in radians. */
    private val pitchDegree: Float
        get() = radianToDegree(orientation.pitch)

    /** Computes the roll in degrees from the current orientation in radians. */
    private val rollDegree: Float
        get() = radianToDegree(orientation.roll)

    /** Checks if the pitch is within the valid tilt range. */
    private val isPitchWithinValidRange: Boolean
        get() = pitchDegree in minTiltAngle..maxTiltAngleRange

    /** Checks if the roll is within the valid tilt range. */
    private val isRollWithinValidRange: Boolean
        get() = rollDegree in minTiltAngle..maxTiltAngleRange

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val placeable = measurable.measure(constraints)

        val (tiltRoll, tiltPitch) = calculateTilt()

        return layout(placeable.width, placeable.height) {
            placeable.placeRelativeWithLayer(
                x = tiltRoll.roundToInt(),
                y = tiltPitch.roundToInt(),
                layerBlock = {
                    // > Note that this definition of yaw, pitch and roll is different from the traditional definition used in aviation where the X axis is along the long side of the plane (tail to nose).
                    // https://developer.android.com/reference/android/hardware/SensorListener.html
                    rotationX = tiltPitch
                    rotationY = tiltRoll
                },
            )
        }
    }

    /**
     * Calculates the tilt values (roll and pitch) based on the current device orientation.
     * If the orientation is out of the valid range, the last known values are used.
     *
     * @return A pair of Floats representing the roll and pitch tilt values.
     */
    private fun calculateTilt(): Pair<Float, Float> {
        return if (isPitchWithinValidRange && isRollWithinValidRange) {
            val tiltPitch = (pitchDegree / 90f) * maxTiltAngle
            val tiltRoll = (rollDegree / 90f).coerceIn(-1f, 1f) * maxTiltAngle

            // Save the current values for future use
            previousTiltRoll = tiltRoll
            previousTiltPitch = tiltPitch

            tiltRoll to tiltPitch
        } else {
            previousTiltRoll to previousTiltPitch
        }
    }

    /**
     * Converts radians to degrees.
     *
     * @param radian The value in radians to convert.
     * @return The value converted to degrees.
     */
    private fun radianToDegree(radian: Float): Float {
        return (radian * 180f / PI).toFloat()
    }
}
