package com.quinn.virginactive

import android.app.Application
import android.util.Log
import com.quinn.virginactive.di.androidModule
import com.quinn.virginactive.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.KoinConfiguration

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin(
            configuration = KoinConfiguration {
                androidContext(this@MainApp)
                Log.i("KOIN", "Context set")
            },
            viewModelModule = androidModule()
        )
    }
}