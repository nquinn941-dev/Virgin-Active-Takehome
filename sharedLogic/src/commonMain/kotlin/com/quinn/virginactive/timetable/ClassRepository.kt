package com.quinn.virginactive.timetable

import com.quinn.virginactive.UserManager
import com.quinn.virginactive.UserState
import com.quinn.virginactive.timetable.mappers.ClassListViewDataMapper
import com.quinn.virginactive.timetable.networking.ClassApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class ClassRepository constructor(
    private val classApi: ClassApi,
    private val classListViewDataMapper: ClassListViewDataMapper,
    private val userManager: UserManager
) {

    private val _classes = MutableStateFlow(ClassListViewData(clubId = "", classesPerDay = emptyMap()))
    val classes: StateFlow<ClassListViewData> = _classes

    suspend fun refreshClasses(clubId: String) {
        val response = classApi.getTimetable(clubId, date = null)
        _classes.value =  classListViewDataMapper.mapToClassListViewData(response)
    }

    suspend fun getClass(classId: String) : Flow<ClassViewData?> {
        return if (_classes.value.clubId.isEmpty()) {
            //Assuming this means havent viewed the timetable yet, so refresh
            val user = (userManager.getUserStateSnapshot() as? UserState.LoggedIn)?.user ?: return emptyFlow()
            refreshClasses(user.clubInfo.id)
             _classes.map {
                it.classesPerDay.flatMap { it.value }.firstOrNull { it.classId == classId }
            }
        } else {
            _classes.map {
                it.classesPerDay.flatMap { it.value }.firstOrNull { it.classId == classId }
            }
        }
    }

    suspend fun bookClass(classId: String): ClassViewData {
        val classToBeBooked =
            _classes.value.classesPerDay.flatMap { it.value }.firstOrNull { it.classId == classId }
                ?: throw IllegalStateException("Couldn't find class for id $classId")
        val user = (userManager.getUserStateSnapshot() as? UserState.LoggedIn)?.user ?: throw IllegalStateException("Not Logged in")

        val response = classApi.bookClass(user.clubInfo.id, classToBeBooked.classId)


        val bookingConfirmationStatus = response.waitlistPosition?.let {
            ClassViewData.ClassConfirmationDetails.ConfirmationStatus.Waitlisted(response.waitlistPosition)
        } ?: ClassViewData.ClassConfirmationDetails.ConfirmationStatus.Confirmed


        val classConfirmationDetails = ClassViewData.ClassConfirmationDetails(
            bookingId = response.bookingId,
            confirmationStatus = bookingConfirmationStatus
        )
        val bookedClassViewData = classListViewDataMapper.mapToClassViewData(response.classInstance, classConfirmationDetails)
        return bookedClassViewData
    }
}