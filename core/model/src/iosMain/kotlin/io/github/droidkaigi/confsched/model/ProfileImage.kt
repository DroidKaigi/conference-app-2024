package io.github.droidkaigi.confsched.model

import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual val ByteArray.imageSize: IntSize
    get() = Bitmap.makeFromImage(
        Image.makeFromEncoded(this),
    ).let {
        IntSize(width = it.width, height = it.height)
    }

actual fun ByteArray.crop(rect: IntRect): ByteArray {
    val image = Image.makeFromEncoded(this)

    val bitmap = Bitmap()
    val imageInfo = image.imageInfo.withWidthHeight(
        width = rect.width,
        height = rect.height,
    )
    bitmap.allocPixels(imageInfo)
    image.readPixels(dst = bitmap, srcX = rect.left, srcY = rect.top)

    val data = Image.makeFromBitmap(bitmap)
        .encodeToData(format = EncodedImageFormat.PNG, quality = 100)
    return requireNotNull(data).bytes
}
