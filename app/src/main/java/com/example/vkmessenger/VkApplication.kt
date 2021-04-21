package com.example.vkmessenger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.vkmessenger.di.appModule
import com.example.vkmessenger.di.serviceModule
import com.example.vkmessenger.di.viewModelModule
import com.example.vkmessenger.util.CHANNEL_ID
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class VkApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@VkApplication)
            androidLogger()
            modules(listOf(viewModelModule, appModule, serviceModule))
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Service_Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}