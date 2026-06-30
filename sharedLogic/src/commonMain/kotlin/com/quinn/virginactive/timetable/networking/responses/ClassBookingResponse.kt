package com.quinn.virginactive.timetable.networking.responses

import kotlinx.serialization.Serializable

@Serializable
data class ClassBookingResponse(
    val bookingId: String,
    val status: String,
    val waitlistPosition: Int?,
    val classInstance: FitnessClass
)