package io.github.droidkaigi.confsched.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import java.io.ByteArrayOutputStream

actual fun generateColoredImageBase64(color: Color, size: IntSize): String {
    val imageColor = android.graphics.Color.argb(
        color.toArgb().alpha,
        color.toArgb().red,
        color.toArgb().green,
        color.toArgb().blue,
    )

    val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    canvas.drawColor(imageColor)

    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}
