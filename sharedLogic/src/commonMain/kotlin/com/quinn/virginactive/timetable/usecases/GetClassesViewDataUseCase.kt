package com.quinn.virginactive.timetable.usecases

import com.quinn.virginactive.timetable.ClassListViewData
import com.quinn.virginactive.timetable.ClassRepository

class GetClassesViewDataUseCase internal constructor(
    private val classRepository: ClassRepository
) {

    suspend fun getClassesViewData(): ClassListViewData {
        return classRepository.getClasses(forceRefresh = false)
    }
}