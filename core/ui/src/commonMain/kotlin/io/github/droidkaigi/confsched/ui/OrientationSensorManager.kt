package io.github.droidkaigi.confsched.ui

import androidx.compose.runtime.Composable

data class Orientation(
    val azimuth: Float,
    val pitch: Float,
    val roll: Float,
) {
    companion object {
        val ZERO = Orientation(0f, 0f, 0f)
    }
}

internal interface OrientationSensorManager {
    val onOrientationChanged: (Orientation) -> Unit
    fun start()
    fun stop()
}

@Composable
internal expect fun getOrientationSensorManager(
    onOrientationChanged: (Orientation) -> Unit,
): OrientationSensorManager
