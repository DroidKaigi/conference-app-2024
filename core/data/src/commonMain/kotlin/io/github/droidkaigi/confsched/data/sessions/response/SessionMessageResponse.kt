package io.github.droidkaigi.confsched.data.sessions.response

import kotlinx.serialization.Serializable

@Serializable
public data class SessionMessageResponse(
    val ja: String,
    val en: String,
)
