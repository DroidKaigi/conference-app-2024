package io.github.droidkaigi.confsched.shared.share

import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

class ShareNavigator {
    fun shareTextWithImage(text: String, image: ImageBitmap) {
        val items = mutableListOf<Any>(text)
        image.toUiImage()?.let {
            items.add(it)
            println("Success to create UIImage from ImageBitmap.")
        } ?: run {
            println("Failed to create UIImage from ImageBitmap.")
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
