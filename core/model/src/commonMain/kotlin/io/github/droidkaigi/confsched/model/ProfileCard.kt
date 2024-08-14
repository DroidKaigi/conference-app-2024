package io.github.droidkaigi.confsched.model

sealed interface ProfileCard {
    data object Loading : ProfileCard

    data object DoesNotExists : ProfileCard

    data class Exists(
        val nickname: String,
        val occupation: String?,
        val link: String?,
        val image: String?,
        val theme: ProfileCardTheme,
    ) : ProfileCard
}

enum class ProfileCardTheme {
    Iguana,
    Hedgehog,
    Giraffe,
    Flamingo,
    Jellyfish,
}
