package io.github.droidkaigi.confsched.model

public actual fun knownPlatformExceptionOrNull(e: Throwable): AppError? {
    return if (e is java.net.UnknownHostException) {
        AppError.InternetConnectionException(e)
    } else {
        null
    }
}
