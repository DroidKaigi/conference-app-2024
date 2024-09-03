package io.github.droidkaigi.confsched.droidkaigiui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun getOrientationSensorManager(
    onOrientationChanged: (Orientation) -> Unit,
): OrientationSensorManager {
    val context = LocalContext.current
    return remember(context, onOrientationChanged) {
        AndroidOrientationSensorManager(
            context = context,
            onOrientationChanged = onOrientationChanged,
        )
    }
}

internal class AndroidOrientationSensorManager(
    context: Context,
    override val onOrientationChanged: (Orientation) -> Unit,
) : OrientationSensorManager, SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    override fun start() {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SENSOR_DELAY,
        )
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SENSOR_DELAY,
        )
    }

    override fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(
                    event.values,
                    0,
                    accelerometerReading,
                    0,
                    accelerometerReading.size,
                )
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            }
        }
        updateOrientationAngles()

        onOrientationChanged(
            Orientation(
                azimuth = orientationAngles[0],
                pitch = orientationAngles[1],
                roll = orientationAngles[2],
            ),
        )
    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading,
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }

    companion object {
        private const val SENSOR_DELAY: Int = SensorManager.SENSOR_DELAY_UI
    }
}
