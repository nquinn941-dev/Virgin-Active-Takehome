package com.quinn.virginactive.di

import com.quinn.virginactive.PlatformDeviceIO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinConfiguration

fun initKoin(configuration: KoinConfiguration? = null, platformDeviceIO: PlatformDeviceIO, platformModule: Module) {
    startKoin {
        configuration?.invoke()
        modules(sharedModule(platformDeviceIO), platformModule)
    }
}