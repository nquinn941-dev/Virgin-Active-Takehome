package com.quinn.virginactive

import android.app.Application
import androidx.core.content.edit
import com.quinn.virginactive.di.androidModule
import com.quinn.virginactive.di.initKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val prefs = this.getSharedPreferences("virgin-active-prefs", MODE_PRIVATE)

        val androidPlatformDeviceIO = object : PlatformDeviceIO {
            override fun read(key: String, default: String?): String? {
                return prefs.getString(key, default)
            }

            override fun write(key: String, value: String?) {
                prefs.edit { putString(key, value) }
            }

            override fun clear(key: String) {
                prefs.edit { remove(key) }
            }
        }

        initKoin(
            platformDeviceIO = androidPlatformDeviceIO,
            platformModule = androidModule()
        )
    }
}