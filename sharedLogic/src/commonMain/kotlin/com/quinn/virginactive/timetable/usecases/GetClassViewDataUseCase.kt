package com.quinn.virginactive.timetable.usecases

import com.quinn.virginactive.timetable.ClassRepository
import com.quinn.virginactive.timetable.ClassViewData
import kotlinx.coroutines.flow.Flow

class GetClassViewDataUseCase internal constructor(
    private val classRepository: ClassRepository
) {
    suspend fun getClassViewData(classId: String): Flow<ClassViewData?> {
        return classRepository.getClass(classId)
    }
}