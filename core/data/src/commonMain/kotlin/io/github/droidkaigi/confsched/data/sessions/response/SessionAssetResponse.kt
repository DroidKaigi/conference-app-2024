package io.github.droidkaigi.confsched.data.sessions.response

import kotlinx.serialization.Serializable

@Serializable
public data class SessionAssetResponse(
    val videoUrl: String?,
    val slideUrl: String?,
)
