package io.github.droidkaigi.confsched.model

data class ProfileCard(
    val nickname: String,
    val occupation: String?,
    val link: String?,
    val imageUri: String?,
    val theme: ProfileCardTheme,
)
