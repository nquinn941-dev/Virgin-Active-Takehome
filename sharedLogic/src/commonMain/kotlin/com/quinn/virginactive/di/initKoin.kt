package com.quinn.virginactive.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.includes

fun initKoin(configuration: KoinConfiguration? = null, viewModelModule: Module) {
    startKoin {
        configuration?.let {
            includes(it)
        }
        modules(platformModule(), sharedModule(), viewModelModule)
    }
}