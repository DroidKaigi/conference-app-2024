package io.github.droidkaigi.confsched.data.eventmap.response

import kotlinx.serialization.Serializable

@Serializable
public data class EventTitleResponse(
    val en: String,
    val ja: String,
)
