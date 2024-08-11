package io.github.droidkaigi.confsched.sessions.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeTableItem(
    @SerialName("timetableItemId")
    val timetableItemId: String
)
