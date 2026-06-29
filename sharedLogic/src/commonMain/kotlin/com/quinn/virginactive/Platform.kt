package com.quinn.virginactive

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform