package io.github.droidkaigi.confsched.data.eventmap.response

import kotlinx.serialization.Serializable

@Serializable
public data class I18nDescResponse(
    val en: String,
    val ja: String,
)
