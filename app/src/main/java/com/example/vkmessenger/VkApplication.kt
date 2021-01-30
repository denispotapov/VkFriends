package com.example.vkmessenger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.vkmessenger.di.DaggerAppComponent
import com.example.vkmessenger.util.CHANNEL_ID
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class VkApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
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