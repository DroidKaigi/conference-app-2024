package io.github.droidkaigi.confsched.sessions.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimetableItemDestination(
    @SerialName("timetableItemId")
    val timetableItemId: String,
)
