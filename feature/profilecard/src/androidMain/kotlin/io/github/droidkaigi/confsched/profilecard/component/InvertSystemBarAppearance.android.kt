package io.github.droidkaigi.confsched.profilecard.component

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

@Composable
internal actual fun InvertSystemBarAppearance() {
    val activity = LocalContext.current as Activity
    val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)

    val originalIsAppearanceLight = remember { insetsController.isAppearanceLightStatusBars }

    DisposableEffect(Unit) {
        insetsController.isAppearanceLightStatusBars = !originalIsAppearanceLight

        onDispose {
            insetsController.isAppearanceLightStatusBars = originalIsAppearanceLight
        }
    }
}
