package com.quinn.virginactive.timetable.usecases

import com.quinn.virginactive.timetable.ClassListViewData
import com.quinn.virginactive.timetable.ClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class GetClassesViewDataUseCase internal constructor(
    private val classRepository: ClassRepository
) {

    suspend fun getClassesViewData(clubId: String) : StateFlow<ClassListViewData> {
        classRepository.refreshClasses(clubId)
        return classRepository.classes
    }
}