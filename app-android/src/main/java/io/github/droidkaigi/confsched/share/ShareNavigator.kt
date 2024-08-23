package io.github.droidkaigi.confsched.share

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import co.touchlab.kermit.Logger
import java.io.File

class ShareNavigator(private val context: Context) {
    fun share(text: String) {
        try {
            ShareCompat.IntentBuilder(context)
                .setText(text)
                .setType("text/plain")
                .startChooser()
        } catch (e: ActivityNotFoundException) {
            Logger.e("ActivityNotFoundException Fail startActivity: $e")
        }
    }

    fun shareTextWithImage(text: String, filePath: String) {
        try {
            val file = File(filePath)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            ShareCompat.IntentBuilder(context)
                .setStream(uri)
                .setText(text)
                .setType("image/png")
                .startChooser()
        } catch (e: ActivityNotFoundException) {
            Logger.e("ActivityNotFoundException Fail startActivity: $e")
        }
    }
}
