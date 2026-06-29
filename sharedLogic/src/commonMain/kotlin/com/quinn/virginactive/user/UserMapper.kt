package com.quinn.virginactive.user

import com.quinn.virginactive.shared.UserResponse

class UserMapper {

    fun mapToUser(response: UserResponse) : User {
        return User(
            id = response.id,
            firstName = response.firstName,
            lastName = response.lastName,
            email = response.email,
            membershipTier = response.membershipTier,
            clubInfo = User.ClubInfo(
                id = response.homeClub.id,
                name = response.homeClub.name
            )
        )
    }
}