package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

interface DeviceOrientationScope {
    val orientation: Orientation
}

private class DeviceOrientationScopeImpl : DeviceOrientationScope {
    override var orientation: Orientation by mutableStateOf(Orientation.Zero)
        private set

    fun updateOrientation(orientation: Orientation) {
        this.orientation = orientation
    }
}

@Composable
fun WithDeviceOrientation(
    content: @Composable (DeviceOrientationScope.() -> Unit),
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val scope = remember {
        DeviceOrientationScopeImpl()
    }
    val sensorManager = getOrientationSensorManager {
        scope.updateOrientation(it)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                sensorManager.start()
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                sensorManager.stop()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    content(scope)
}
