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
    val status: ClassStatus,
    val startsWithin12Hours: Boolean,
    val confirmationDetails: ClassConfirmationDetails?
) {
    data class ClassConfirmationDetails(
        val bookingId: String
    )
}
