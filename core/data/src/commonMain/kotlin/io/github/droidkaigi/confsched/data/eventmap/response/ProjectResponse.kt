package io.github.droidkaigi.confsched.data.eventmap.response

import kotlinx.serialization.Serializable

@Serializable
public data class ProjectResponse(
    val i18nDesc: I18nDescResponse,
    val id: String,
    val message: MessageResponse?,
    val moreDetailsUrl: String?,
    val noShow: Boolean,
    val roomId: Int,
    val title: EventTitleResponse,
)
