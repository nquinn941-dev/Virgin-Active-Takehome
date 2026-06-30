package com.quinn.virginactive.timetable.usecases

import com.quinn.virginactive.timetable.ClassRepository
import com.quinn.virginactive.timetable.ClassViewData
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow

class AlreadyBookedException : Exception()

class ClassInPastException : Exception()

class BookClassUseCase internal constructor(
    private val classRepository: ClassRepository
) {

    suspend fun bookClass(classId: String) : ClassViewData {
        return try {
            classRepository.bookClass(classId)
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (ex is ClientRequestException) {
                when(ex.response.status) {
                    HttpStatusCode.Conflict -> throw AlreadyBookedException()
                    HttpStatusCode.UnprocessableEntity -> throw ClassInPastException()
                    else -> throw ex
                }
            } else {
                throw ex
            }
        }
    }
}
