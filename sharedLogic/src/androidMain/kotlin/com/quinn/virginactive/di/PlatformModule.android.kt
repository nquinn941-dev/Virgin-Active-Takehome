package com.quinn.virginactive.di

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.provider.CalendarContract
import androidx.core.content.edit
import com.quinn.virginactive.LocalClassReminder
import com.quinn.virginactive.PlatformDeviceIO
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.math.sin

class AndroidLocalClassReminder(
    private val context: Context
) : LocalClassReminder {
    override fun setLocalReminder(
        title: String,
        location: String,
        startTime: Long,
        endTime: Long
    ) {
        val intent = Intent(Intent.ACTION_INSERT).addFlags(FLAG_ACTIVITY_NEW_TASK).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}

class AndroidDeviceIO(
    private val context: Context
) : PlatformDeviceIO {
    val prefs = context.getSharedPreferences("virgin-active-prefs", Context.MODE_PRIVATE)

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

actual fun platformModule() = module {

    single {
        AndroidDeviceIO(
            context = androidContext()
        )
    }.bind<PlatformDeviceIO>()

    single {
        AndroidLocalClassReminder(
            context = androidContext()
        )
    }.bind<LocalClassReminder>()
}