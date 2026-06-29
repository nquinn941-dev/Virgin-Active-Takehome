package com.quinn.virginactive.auth.responses

import com.quinn.virginactive.shared.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
    val user: UserResponse
)
