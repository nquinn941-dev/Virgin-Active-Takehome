package com.quinn.virginactive

interface PlatformDeviceIO {

    fun read(key: String, default: String?) : String?
    fun write(key: String, value: String?)
    fun clear(key: String)
}