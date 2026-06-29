package com.quinn.virginactive.user

import com.quinn.virginactive.shared.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface ProfileApi {
    suspend fun getProfile() : UserResponse
}

internal class KtorProfileApi(
    val httpClient: HttpClient
) : ProfileApi {
    private val baseUrl = "http://10.0.2.2:8080"

    override suspend fun getProfile(): UserResponse =
        httpClient.get(urlString = "$baseUrl/me") {
            contentType(ContentType.Application.Json)
        }.body()
}