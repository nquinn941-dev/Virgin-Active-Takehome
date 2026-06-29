package com.quinn.virginactive.timetable

import com.quinn.virginactive.timetable.mappers.ClassListViewDataMapper
import com.quinn.virginactive.timetable.networking.ClassApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

internal class ClassRepository constructor(
    private val classApi: ClassApi,
    private val classListViewDataMapper: ClassListViewDataMapper
) {

    private val _classes = MutableStateFlow(ClassListViewData(clubId = "", classesPerDay = emptyMap()))
    val classes: StateFlow<ClassListViewData> = _classes

    suspend fun refreshClasses(clubId: String) {
        val response = classApi.getTimetable(clubId, date = null)
        _classes.value =  classListViewDataMapper.mapToClassListViewData(response)
    }

    suspend fun getClass(classId: String) : Flow<ClassViewData?> {
        return _classes.map {
            it.classesPerDay.flatMap { it.value }.firstOrNull { it.classId == classId }
        }
    }
}