package com.quinn.virginactive.timetable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponse(
    val clubId: String,
    val weekStart: String,
    val weekEnd: String,
    val selectedDate: String,
    val days: List<DaySchedule>
)

@Serializable
data class DaySchedule(
    val date: String,
    val classes: List<FitnessClass>
)

@Serializable
data class FitnessClass(
    val classId: String,
    val clubId: String,
    val title: String,
    val trainer: String,
    val type: ClassType,
    val startsAt: String,
    val endsAt: String,
    val spots: Int,
    val available: Int,
    val waitlistCount: Int,
    val status: ClassStatus,
    val userBookingStatus: UserBookingStatus
)

@Serializable
enum class ClassType {
    @SerialName("yoga")
    YOGA,

    @SerialName("spin")
    SPIN,

    @SerialName("hiit")
    HIIT,

    @SerialName("pilates")
    PILATES,

    @SerialName("swimming")
    SWIMMING,

    @SerialName("groupWorkout")
    GROUP_WORKOUT
}

@Serializable
enum class ClassStatus {
    @SerialName("available")
    AVAILABLE,

    @SerialName("full")
    FULL
}

@Serializable
enum class UserBookingStatus {
    @SerialName("none")
    NONE,
    @SerialName("booked")
    BOOKED,
    @SerialName("waitlisted")
    WAITLISTED
}
