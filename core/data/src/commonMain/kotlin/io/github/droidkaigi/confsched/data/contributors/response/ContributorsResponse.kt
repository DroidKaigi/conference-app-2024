package io.github.droidkaigi.confsched.data.contributors.response

import kotlinx.serialization.Serializable

@Serializable
public data class ContributorsResponse(
    val status: String,
    val contributors: List<ContributorResponse>,
)

@Serializable
public data class ContributorResponse(
    val id: Int,
    val username: String,
    val iconUrl: String,
)
