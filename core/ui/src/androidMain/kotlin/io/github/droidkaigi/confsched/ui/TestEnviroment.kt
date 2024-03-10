package io.github.droidkaigi.confsched.ui

import android.os.Build

actual fun isTest(): Boolean {
    return "robolectric" == Build.FINGERPRINT
}
