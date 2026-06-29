package com.quinn.virginactive.timetable.networking

import com.quinn.virginactive.timetable.ClassesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface ClassApi {
    suspend fun getTimetable(clubId: String, date: String?) : ClassesResponse
}

internal class KtorClassApi(
    private val httpClient: HttpClient
) : ClassApi {

    private val baseUrl = "http://10.0.2.2:8080"

    override suspend fun getTimetable(
        clubId: String,
        date: String?
    ): ClassesResponse =
        httpClient.get(urlString = "${baseUrl}/clubs/$clubId/classes/timetable") {
            date?.let {
                parameter("date", it)
            }
            contentType(ContentType.Application.Json)
        }.body()

}