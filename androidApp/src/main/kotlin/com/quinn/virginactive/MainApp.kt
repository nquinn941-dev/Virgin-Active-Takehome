package com.quinn.virginactive

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
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

        val androidLocalClassReminder = object : LocalClassReminder {
            override fun setLocalReminder(
                title: String,
                location: String,
                startTime: Long,
                endTime: Long
            ) {
                val intent = Intent(Intent.ACTION_INSERT).addFlags(FLAG_ACTIVITY_NEW_TASK).apply {
                    data = android.provider.CalendarContract.Events.CONTENT_URI
                    putExtra(android.provider.CalendarContract.Events.TITLE, title)
                    putExtra(android.provider.CalendarContract.Events.EVENT_LOCATION, location)
                    putExtra(android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                    putExtra(android.provider.CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        initKoin(
            platformDeviceIO = androidPlatformDeviceIO,
            platformModule = androidModule(androidLocalClassReminder)
        )
    }
}