package io.github.droidkaigi.confsched.data.profilecard

import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardType
import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileCardJson(
    val nickname: String,
    val occupation: String,
    val link: String,
    val image: String,
    val theme: String,
)

internal fun ProfileCardJson.toModel() = ProfileCard.Exists(
    nickname = nickname,
    occupation = occupation,
    link = link,
    image = image,
    cardType = theme.toProfileCardTheme(),
)

internal fun String.toProfileCardTheme() = ProfileCardType.valueOf(this)

internal fun ProfileCard.Exists.toJson() = ProfileCardJson(
    nickname = nickname,
    occupation = occupation,
    link = link,
    image = image,
    theme = cardType.name,
)
