package io.github.droidkaigi.confsched.data.eventmap.response

import kotlinx.serialization.Serializable

@Serializable
public data class EventMapResponse(
    val projects: List<ProjectResponse> = emptyList(),
    val rooms: List<RoomResponse> = emptyList(),
    val status: String = "",
)
