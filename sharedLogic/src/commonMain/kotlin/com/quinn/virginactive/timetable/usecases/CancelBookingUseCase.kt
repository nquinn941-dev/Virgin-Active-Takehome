package com.quinn.virginactive.timetable.usecases

import com.quinn.virginactive.timetable.ClassRepository

class CancelBookingUseCase internal constructor(
    private val classRepository: ClassRepository
) {

    suspend fun cancelBooking(classId: String) {
        classRepository.cancelClassBooking(classId)
    }
}