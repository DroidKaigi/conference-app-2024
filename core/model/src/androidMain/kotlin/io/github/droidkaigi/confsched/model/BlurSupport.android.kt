package io.github.droidkaigi.confsched.model

import android.os.Build

public actual fun isBlurSupported(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
