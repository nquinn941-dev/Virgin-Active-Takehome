package com.quinn.virginactive.user

import com.quinn.virginactive.shared.MembershipTier

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val membershipTier: MembershipTier,
    val clubInfo: ClubInfo
) {

    data class ClubInfo(
        val id: String,
        val name: String
    )
}
