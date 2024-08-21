package io.github.droidkaigi.confsched.ui

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import io.github.droidkaigi.confsched.AppContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.io.FileOutputStream

actual fun ImageBitmap.saveToDisk(): String {
    val timestamp = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
        .toString()
        .replace(":", "")
        .replace(".", "")
    val fileName = "shared_image_$timestamp.png"

    val cachePath = File(AppContext.get().cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, fileName)
    val outputStream = FileOutputStream(file)

    this.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return file.absolutePath
}
