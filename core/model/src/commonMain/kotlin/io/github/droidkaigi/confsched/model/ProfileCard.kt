package io.github.droidkaigi.confsched.model

sealed interface ProfileCard {
    data object Loading : ProfileCard

    data object DoesNotExists : ProfileCard

    data class Exists(
        val nickname: String,
        val occupation: String,
        val link: String,
        val image: String,
        val theme: ProfileCardTheme,
    ) : ProfileCard {
        public companion object
    }
}

enum class ProfileCardTheme {
    Iguana,
    Hedgehog,
    Giraffe,
    Flamingo,
    Jellyfish,
    None,
}

public fun ProfileCard.Exists.Companion.fake(): ProfileCard.Exists {
    return ProfileCard.Exists(
        nickname = "test",
        occupation = "test",
        link = "test",
        image = generateWhiteImageBase64(),
        theme = ProfileCardTheme.Iguana,
    )
}
