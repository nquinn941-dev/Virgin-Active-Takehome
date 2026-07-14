package com.quinn.virginactive.home

import com.quinn.virginactive.home.responses.HomeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface HomeApi {

    suspend fun getHomeManifest() : HomeResponse
}

internal class KtorHomeApi(
    private val httpClient: HttpClient
): HomeApi {

    private val baseUrl = "http://10.0.2.2:8080"


    override suspend fun getHomeManifest(): HomeResponse =
        httpClient.get(urlString = "$baseUrl/home/manifest") {
            contentType(ContentType.Application.Json)
            expectSuccess = true
        }.body()

}