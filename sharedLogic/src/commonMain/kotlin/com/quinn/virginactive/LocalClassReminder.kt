package com.quinn.virginactive

interface LocalClassReminder {

    fun setLocalReminder(
        title: String,
        location:String,
        startTime: Long,
        endTime: Long
    )
}