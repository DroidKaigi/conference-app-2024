package io.github.droidkaigi.confsched.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.unit.IntRect
import java.io.ByteArrayOutputStream

actual fun ByteArray.crop(rect: IntRect): ByteArray {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, size)
    val cropped = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width, rect.height)

    return ByteArrayOutputStream(cropped.byteCount).use { stream ->
        cropped.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.toByteArray()
    }
}
