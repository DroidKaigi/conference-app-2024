package io.github.droidkaigi.confsched.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import java.io.ByteArrayOutputStream

actual val ByteArray.imageSize: IntSize
    get() = this.asBitmap().let {
        IntSize(width = it.width, height = it.height)
    }

actual fun ByteArray.crop(rect: IntRect): ByteArray {
    val cropped = Bitmap.createBitmap(this.asBitmap(), rect.left, rect.top, rect.width, rect.height)

    return ByteArrayOutputStream(cropped.byteCount).use { stream ->
        cropped.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.toByteArray()
    }
}

private fun ByteArray.asBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}
