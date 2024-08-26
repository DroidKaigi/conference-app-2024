package io.github.droidkaigi.confsched.droidkaigiui

import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = VERSION_CODES.O_MR1)
actual fun canShowLargeVector(): Boolean {
    return Build.VERSION.SDK_INT > 26
}
