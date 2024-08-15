package io.github.droidkaigi.confsched.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import androidx.compose.ui.unit.IntSize
import java.io.ByteArrayOutputStream

internal actual fun generateWhiteImageBase64(size: IntSize): String {
    val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    canvas.drawColor(android.graphics.Color.WHITE)

    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}
