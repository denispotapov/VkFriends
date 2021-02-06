package com.example.vkmessenger.service

import android.app.Notification
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.vkmessenger.R
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineActivity
import com.example.vkmessenger.util.CHANNEL_ID
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class StatusTrackingService : JobService() {

    @Inject
    lateinit var interactor: StatusTrackingServiceInteractor
    private lateinit var observer: Observer<List<Friend>>

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        Timber.d("Job сервис onCreate запущен")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.d("Job сервис onStartJob запущен")

        interactor.getTrackingFriendsOnline()
        observer =
            Observer<List<Friend>> { listOnlineFriend ->
                for (onlineFriend in listOnlineFriend) {
                    showNotificationUserOnline(onlineFriend, onlineFriend.id)
                    Timber.d("Job $listOnlineFriend")
                }
                jobFinished(params, false)
            }
        interactor.trackingFriendsOnline.observeForever(observer)

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Timber.d("Job сервис onStopJob запущен")
        return false
    }

    private fun showNotificationUserOnline(friend: Friend, noteId: Int) {
        val scope = CoroutineScope(Dispatchers.IO)

        val openActivityIntent = Intent(this, FriendsOnlineActivity::class.java)
        val openActivityPendingIntent = PendingIntent.getActivity(
            this, 0, openActivityIntent, 0
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Служба отслеживания статуса")
            .setContentText("${friend.firstName} ${friend.lastName} онлайн")
            .setSmallIcon(R.drawable.ic_android_black)
            .setSound(defaultSoundUri)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openActivityPendingIntent)

        scope.launch {
            val userPhoto =
                Glide.with(this@StatusTrackingService)
                    .asBitmap()
                    .load(friend.photo)
                    .submit()
                    .get()

            withContext(Dispatchers.Main) {
                notification.setLargeIcon(userPhoto)
                NotificationManagerCompat.from(this@StatusTrackingService)
                    .notify(noteId, notification.build())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("Job сервис onDestroy уничтожен")
        interactor.trackingFriendsOnline.removeObserver(observer)
    }
}
