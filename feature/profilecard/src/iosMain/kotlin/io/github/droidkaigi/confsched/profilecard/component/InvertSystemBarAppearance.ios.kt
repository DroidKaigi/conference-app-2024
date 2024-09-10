package io.github.droidkaigi.confsched.profilecard.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle.UIUserInterfaceStyleDark
import platform.UIKit.UIUserInterfaceStyle.UIUserInterfaceStyleLight
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

@Composable
internal actual fun InvertSystemBarAppearance() {
    val application = UIApplication.sharedApplication
    val scene = application.connectedScenes.firstOrNull() as? UIWindowScene
    val window = scene?.windows?.firstOrNull() as? UIWindow ?: return

    val originalUserInterfaceStyle = remember { window.overrideUserInterfaceStyle }

    DisposableEffect(Unit) {
        window.overrideUserInterfaceStyle = if (originalUserInterfaceStyle == UIUserInterfaceStyleDark) {
            UIUserInterfaceStyleLight
        } else {
            UIUserInterfaceStyleDark
        }

        onDispose { window.overrideUserInterfaceStyle = originalUserInterfaceStyle }
    }
}
