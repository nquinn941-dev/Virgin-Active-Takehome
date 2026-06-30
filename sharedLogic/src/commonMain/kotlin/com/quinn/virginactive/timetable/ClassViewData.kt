package com.quinn.virginactive.timetable

data class ClassListViewData(
    val clubId: String,
    val classesPerDay: Map<String, List<ClassViewData>>
)

data class ClassViewData(
    val classId: String,
    val title: String,
    val trainer: String,
    val classDisplayType: String,
    val date: String,
    val time: String,
    val availability: String,
    val waitlistCount: Int,
    val bookingStatus: BookingStatus,
    val startsWithin12Hours: Boolean,
    val confirmationDetails: ClassConfirmationDetails?,
    val isInPast: Boolean
) {
    data class ClassConfirmationDetails(
        val bookingId: String,
        val confirmationStatus: ConfirmationStatus
    ) {
        sealed class ConfirmationStatus {
            data object Confirmed : ConfirmationStatus()
            data class Waitlisted(val waitlistPosition: Int) : ConfirmationStatus()
        }
    }

    enum class BookingStatus {
        OPEN,
        FULL,
        BOOKED,
        WAITLISTED
    }
}
