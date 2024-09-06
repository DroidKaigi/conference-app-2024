package io.github.droidkaigi.confsched.shared.share

import androidx.compose.ui.graphics.ImageBitmap
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched.droidkaigiui.toUiImage
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

class ShareNavigator {
    fun share(text: String) {
        val items = mutableListOf<Any>(text)

        val activityViewController = UIActivityViewController(items, null)
        val keyWindow = UIApplication.sharedApplication.keyWindow
        val rootViewController = keyWindow?.rootViewController

        rootViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }

    fun shareTextWithImage(text: String, image: ImageBitmap) {
        val items = mutableListOf<Any>(text)
        image.toUiImage()?.let {
            items.add(it)
            Logger.d("Success to create UIImage from ImageBitmap.")
        } ?: run {
            Logger.d("Failed to create UIImage from ImageBitmap.")
        }

        val activityViewController = UIActivityViewController(items, null)
        val keyWindow = UIApplication.sharedApplication.keyWindow
        val rootViewController = keyWindow?.rootViewController

        rootViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
    }
}
