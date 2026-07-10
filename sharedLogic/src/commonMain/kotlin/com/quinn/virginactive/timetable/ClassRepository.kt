package com.quinn.virginactive.timetable

import com.quinn.virginactive.UserManager
import com.quinn.virginactive.UserState
import com.quinn.virginactive.timetable.mappers.ClassListViewDataMapper
import com.quinn.virginactive.timetable.networking.ClassApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

internal class ClassRepository constructor(
    private val classApi: ClassApi,
    private val classListViewDataMapper: ClassListViewDataMapper,
    private val userManager: UserManager,
    private val cacheDuration: Duration = 5.minutes
) {

    private val _classes = MutableStateFlow(ClassListViewData(clubId = "", classesPerDay = emptyMap()))
//    val classes: StateFlow<ClassListViewData> = _classes
    private val refreshMutex = Mutex()
    private var lastFetchedAt: Instant? = null

    init {
        userManager.observeUserState().distinctUntilChangedBy { it::class }.map {
            lastFetchedAt = null
        }
    }

    suspend fun getClasses(clubId: String, forceRefresh: Boolean) : ClassListViewData {
        val cacheIsValid =
            !forceRefresh && lastFetchedAt != null && lastFetchedAt!!.plus(cacheDuration) > Clock.System.now()
        if (cacheIsValid) return _classes.value
        val response = classApi.getTimetable(clubId, date = null)
        _classes.value =  classListViewDataMapper.mapToClassListViewData(response)
        lastFetchedAt = Clock.System.now()
        return _classes.value
    }

    suspend fun refreshClasses(clubId: String, forceRefresh: Boolean = false) {
        refreshMutex.withLock {
            val cacheIsValid =
                !forceRefresh && lastFetchedAt != null && lastFetchedAt!!.plus(cacheDuration) < Clock.System.now()

            if (cacheIsValid) return
        }
        val response = classApi.getTimetable(clubId, date = null)
        _classes.value =  classListViewDataMapper.mapToClassListViewData(response)
        lastFetchedAt = Clock.System.now()
    }

    private fun invalidateCache() {
        lastFetchedAt = null
    }

    suspend fun getClass(classId: String) : ClassViewData? {
        return if (_classes.value.clubId.isEmpty()) {
            //Assuming this means havent viewed the timetable yet, so refresh
            val user = (userManager.getUserStateSnapshot() as? UserState.LoggedIn)?.user ?: return null
            val classes = getClasses(user.clubInfo.id, forceRefresh = true)
            classes.classesPerDay.flatMap { it.value }.firstOrNull { it.classId == classId }
//             _classes.map {
//                it.classesPerDay.flatMap { it.value }.firstOrNull { it.classId == classId }
//            }
        } else {
            _classes.value.classesPerDay.flatMap { it.value }.firstOrNull { it.classId == classId }
        }
    }

    suspend fun bookClass(classId: String): ClassViewData {
        val user = (userManager.getUserStateSnapshot() as? UserState.LoggedIn)?.user ?: throw IllegalStateException("Not Logged in")

        val response = classApi.bookClass(user.clubInfo.id, classId)


        val bookingConfirmationStatus = response.waitlistPosition?.let {
            ClassViewData.ClassConfirmationDetails.ConfirmationStatus.Waitlisted(response.waitlistPosition)
        } ?: ClassViewData.ClassConfirmationDetails.ConfirmationStatus.Confirmed


        val classConfirmationDetails = ClassViewData.ClassConfirmationDetails(
            bookingId = response.bookingId,
            confirmationStatus = bookingConfirmationStatus
        )
        val bookedClassViewData = classListViewDataMapper.mapToClassViewData(response.classInstance, classConfirmationDetails)
        invalidateCache()
        return bookedClassViewData
    }

    suspend fun cancelClassBooking(classId: String): ClassViewData {
        val user = (userManager.getUserStateSnapshot() as? UserState.LoggedIn)?.user ?: throw IllegalStateException("Not Logged in")
        classApi.cancelBooking(user.clubInfo.id, classId)
        val classes = getClasses(user.clubInfo.id, forceRefresh = true)
        return classes.classesPerDay.flatMap { it.value }.first { it.classId == classId }
    }
}