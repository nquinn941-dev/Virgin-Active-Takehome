package com.quinn.virginactive

import kotlin.time.Instant

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiry: Instant
)
