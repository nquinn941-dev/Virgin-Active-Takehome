package com.quinn.virginactive

import com.quinn.virginactive.auth.AuthApi
import com.quinn.virginactive.auth.request.RefreshRequest
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

internal class TokenStore constructor(
    private val platformDeviceIO: PlatformDeviceIO,
    private val authApi: AuthApi
) {
    private val ACCESS_TOKEN_KEY = "access_token"
    private val REFRESH_TOKEN_KEY = "refresh_token"
    private val EXPIRY_DATE_KEY = "expiry_date"


    suspend fun getToken() : AuthToken? {
        val accessToken = platformDeviceIO.read(ACCESS_TOKEN_KEY, null) ?: return null
        val refreshToken = platformDeviceIO.read(REFRESH_TOKEN_KEY, null) ?: return null
        val expiryString = platformDeviceIO.read(EXPIRY_DATE_KEY, null) ?: return null

        val expiryInstant = Instant.parse(expiryString)

        return if (expiryInstant.minus(60.seconds) < Clock.System.now()) {
            refresh(refreshToken)
        } else {
            AuthToken(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiry = expiryInstant
            )
        }
    }

    suspend fun saveToken(token: AuthToken) {
        token.save()
    }

    suspend fun AuthToken.save() {
        platformDeviceIO.write(ACCESS_TOKEN_KEY, accessToken)
        platformDeviceIO.write(REFRESH_TOKEN_KEY, refreshToken)
        platformDeviceIO.write(EXPIRY_DATE_KEY, expiry.toString())
    }

    fun clearToken() {
        platformDeviceIO.clear(ACCESS_TOKEN_KEY)
        platformDeviceIO.clear(REFRESH_TOKEN_KEY)
        platformDeviceIO.clear(EXPIRY_DATE_KEY)
    }

    suspend fun refresh() : AuthToken? {
        val refreshToken = platformDeviceIO.read(REFRESH_TOKEN_KEY, null) ?: return null
        return refresh(refreshToken)
    }

    private suspend fun refresh(refreshToken: String) : AuthToken? {
        val request = RefreshRequest(refreshToken)
        return try {
            val response = authApi.refresh(request = request)
            val newToken = AuthToken(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                expiry = Clock.System.now().plus(response.expiresIn.seconds)
            )
            newToken.save()
            newToken
        } catch (ex: Exception) {
            clearToken()
            null
        }

    }
}