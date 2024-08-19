package io.github.droidkaigi.confsched.model

sealed interface Settings {
    data object Loading : Settings

    data class Exists(
        val useFontFamily: FontFamily,
        val enableAnimation: Boolean,
        val enableFallbackMode: Boolean,
    ) : Settings

    companion object {
        fun defaultValue() = Exists(
            useFontFamily = FontFamily.DotGothic16Regular,
            enableAnimation = true,
            enableFallbackMode = false,
        )
    }
}
