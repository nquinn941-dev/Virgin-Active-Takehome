package com.quinn.virginactive.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val membershipTier: MembershipTier,
    val homeClub: UserHomeClubResponse
)

@Serializable
enum class MembershipTier {
    @SerialName("premium")
    Premium,
    @SerialName("essential")
    Essential,
}

@Serializable
data class UserHomeClubResponse(
    val id: String,
    val name: String
)
