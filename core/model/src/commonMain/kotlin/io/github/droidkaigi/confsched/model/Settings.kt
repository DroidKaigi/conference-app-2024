package io.github.droidkaigi.confsched.model

sealed interface Settings {
    data object Loading : Settings

    data object DoesNotExists : Settings

    data class Exists(
        val useFontFamilyName: String,
    ) : Settings
}
