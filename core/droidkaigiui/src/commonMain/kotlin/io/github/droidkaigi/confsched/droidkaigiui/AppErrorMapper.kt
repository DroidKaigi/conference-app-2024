package io.github.droidkaigi.confsched.droidkaigiui

internal fun Throwable.toApplicationErrorMessage(
    composeResourceErrorMessages: List<ComposeResourceErrorMessage>? = null,
): String {
    composeResourceErrorMessages?.forEach {
        if (it.appErrorClass.isInstance(this)) return it.message
    }
    return message ?: ""
}
