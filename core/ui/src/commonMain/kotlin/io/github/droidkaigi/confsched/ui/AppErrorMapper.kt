package io.github.droidkaigi.confsched.ui

fun Throwable.toApplicationErrorMessage(): String {
    return message ?: ""
}
