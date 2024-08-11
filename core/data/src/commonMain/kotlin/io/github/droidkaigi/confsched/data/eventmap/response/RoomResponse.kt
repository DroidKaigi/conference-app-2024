package io.github.droidkaigi.confsched.data.eventmap.response

import kotlinx.serialization.Serializable

@Serializable
public data class RoomResponse(
    val id: Int,
    val name: RoomNameResponse,
    val sort: Int,
)
