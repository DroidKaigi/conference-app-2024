package io.github.droidkaigi.confsched.share

import android.content.Context
import android.graphics.Bitmap.CompressFormat.PNG
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import kotlinx.datetime.Clock.System
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.io.FileOutputStream

fun ImageBitmap.saveToDisk(context: Context): String {
    val timestamp = System.now()
        .toLocalDateTime(TimeZone.UTC)
        .toString()
        .replace(":", "")
        .replace(".", "")
    val fileName = "shared_image_$timestamp.png"

    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, fileName)
    val outputStream = FileOutputStream(file)

    this.asAndroidBitmap().compress(PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return file.absolutePath
}
