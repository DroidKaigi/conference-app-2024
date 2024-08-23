package io.github.droidkaigi.confsched.shared.share

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGImageAlphaInfo
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUiImage(): UIImage? {
    val buffer = IntArray(width * height)
    readPixels(buffer)

    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = CGBitmapContextCreate(
        data = buffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
    )

    val cgImage = CGBitmapContextCreateImage(context)
    return cgImage?.let { UIImage.imageWithCGImage(it) }
}
