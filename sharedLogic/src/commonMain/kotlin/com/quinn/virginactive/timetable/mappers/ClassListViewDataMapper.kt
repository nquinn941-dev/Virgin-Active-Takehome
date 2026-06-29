package com.quinn.virginactive.timetable.mappers

import com.quinn.virginactive.timetable.ClassListViewData
import com.quinn.virginactive.timetable.ClassViewData
import com.quinn.virginactive.timetable.ClassesResponse
import com.quinn.virginactive.timetable.FitnessClass
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

    private fun mapToClassViewData(fitnessClass: FitnessClass) : ClassViewData {
        val startTimeInstant = Instant.parse(fitnessClass.startsAt)
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
            status = fitnessClass.status,
            startsWithin12Hours = startTimeInstant.minus(12.hours) < Clock.System.now(),
            confirmationDetails = null
        )
    }
}