package com.quinn.virginactive.timetable.mappers

import com.quinn.virginactive.timetable.ClassListViewData
import com.quinn.virginactive.timetable.networking.responses.ClassStatus
import com.quinn.virginactive.timetable.ClassViewData
import com.quinn.virginactive.timetable.networking.responses.ClassesResponse
import com.quinn.virginactive.timetable.networking.responses.FitnessClass
import com.quinn.virginactive.timetable.networking.responses.UserBookingStatus
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

class ClassListViewDataMapper constructor() {

    fun mapToClassListViewData(classListResponse: ClassesResponse) : ClassListViewData {
        return ClassListViewData(
            clubId = classListResponse.clubId,
            classesPerDay = classListResponse.days.associate { day ->
                day.date to day.classes.map { mapToClassViewData(it) }

            }
        )
    }

    fun mapToClassViewData(fitnessClass: FitnessClass, confirmationDetails: ClassViewData.ClassConfirmationDetails? = null) : ClassViewData {
        val startTimeInstant = Instant.parse(fitnessClass.startsAt)
        val endTimeInstant = Instant.parse(fitnessClass.endsAt)
        val isInPast = startTimeInstant < Clock.System.now()
        return ClassViewData(
            classId = fitnessClass.classId,
            title = fitnessClass.title,
            trainer = fitnessClass.trainer,
            classDisplayType = fitnessClass.type.name.replace("_", " ").lowercase()
                .replaceFirstChar { it.titlecase() },
            date = fitnessClass.startsAt.substringBefore('T'),
            time = "${
                fitnessClass.startsAt.substringAfter("T").substringBefore("+")
            } - ${fitnessClass.endsAt.substringAfter("T").substringBefore("+")}",
            availability = "${fitnessClass.available} of ${fitnessClass.spots} spots available",
            waitlistCount = fitnessClass.waitlistCount,
            bookingStatus = fitnessClass.toBookingStatus(),
            startsWithin12Hours = startTimeInstant.minus(12.hours) < Clock.System.now() && !isInPast,
            confirmationDetails = confirmationDetails,
            isInPast = isInPast,
            startTimeEpoch = startTimeInstant.toEpochMilliseconds(),
            endTimeEpoch = endTimeInstant.toEpochMilliseconds()
        )
    }

    private fun FitnessClass.toBookingStatus() : ClassViewData.BookingStatus {
        return when {
            userBookingStatus == UserBookingStatus.WAITLISTED -> ClassViewData.BookingStatus.WAITLISTED
            userBookingStatus == UserBookingStatus.BOOKED -> ClassViewData.BookingStatus.BOOKED
            status == ClassStatus.AVAILABLE -> ClassViewData.BookingStatus.OPEN
            status == ClassStatus.FULL -> ClassViewData.BookingStatus.FULL
            else -> ClassViewData.BookingStatus.OPEN
        }
    }
}