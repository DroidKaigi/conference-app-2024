package io.github.droidkaigi.confsched.shared

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

fun openSettingsApp() {
    val settingsUrl = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
    if (settingsUrl != null) {
        UIApplication.sharedApplication.openURL(settingsUrl)
    }
}
