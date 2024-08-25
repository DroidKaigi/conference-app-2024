package io.github.droidkaigi.confsched.droidkaigiui

import android.os.Build

actual fun isTest(): Boolean {
    return "robolectric" == Build.FINGERPRINT
}
