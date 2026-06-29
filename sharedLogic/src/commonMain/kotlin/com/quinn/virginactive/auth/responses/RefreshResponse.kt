package com.quinn.virginactive.auth.responses

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)