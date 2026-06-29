package com.quinn.virginactive.auth

import com.quinn.virginactive.auth.request.LoginRequest
import com.quinn.virginactive.auth.request.RefreshRequest
import com.quinn.virginactive.auth.responses.LoginResponse
import com.quinn.virginactive.auth.responses.RefreshResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal interface AuthApi {

    suspend fun login(request: LoginRequest) : LoginResponse

    suspend fun refresh(request: RefreshRequest) : RefreshResponse
}


internal class KtorAuthApi(
    val httpClient: HttpClient
) : AuthApi {

    private val baseUrl = "http://10.0.2.2:8080"


    override suspend fun login(request: LoginRequest): LoginResponse =
        httpClient.post(
            urlString = "$baseUrl/auth/login"
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun refresh(request: RefreshRequest): RefreshResponse =
        httpClient.post(
            urlString = "$baseUrl/auth/refresh"
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

}