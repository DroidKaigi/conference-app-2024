package io.github.droidkaigi.confsched.shared.share

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateWithName
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.kCGBitmapByteOrder32Little
import platform.CoreGraphics.kCGColorSpaceSRGB
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUiImage(): UIImage? {
    val buffer = IntArray(width * height)
    readPixels(buffer)

    // https://github.com/takahirom/roborazzi/blob/main/roborazzi-compose-ios/src/iosMain/kotlin/io/github/takahirom/roborazzi/RoborazziIos.kt#L88C51-L88C68
    val colorSpace = CGColorSpaceCreateWithName(kCGColorSpaceSRGB)
    val bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst.value or kCGBitmapByteOrder32Little
    val context = CGBitmapContextCreate(
        data = buffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = bitmapInfo,
    )

    val cgImage = CGBitmapContextCreateImage(context)
    return cgImage?.let { UIImage.imageWithCGImage(it) }
}
