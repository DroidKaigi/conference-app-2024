package io.github.droidkaigi.confsched.model

data class ProfileCard(
    val nickname: String,
    val occupation: String?,
    val link: String?,
    val image: String?,
    val theme: ProfileCardTheme,
)

enum class ProfileCardTheme {
    Iguana,
    Hedgehog,
    Giraffe,
    Flamingo,
    Jellyfish,
}
