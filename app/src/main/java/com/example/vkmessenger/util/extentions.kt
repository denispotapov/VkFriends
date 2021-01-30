package com.example.vkmessenger.util

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.vkmessenger.R
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineActivity
import kotlinx.coroutines.*

fun Context.showNotificationUserOnline(fistName: String, lastName: String, photoUrl: String) {

    val scope = CoroutineScope(Dispatchers.IO)

    val openActivityIntent = Intent(this, FriendsOnlineActivity::class.java)
    val openActivityPendingIntent = PendingIntent.getActivity(
        this, 0, openActivityIntent, 0
    )

    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Служба отслеживания статуса")
        .setContentText("$fistName $lastName онлайн")
        .setSmallIcon(R.drawable.ic_android_black)
        .setSound(defaultSoundUri)
        .setDefaults(Notification.DEFAULT_LIGHTS)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(openActivityPendingIntent)

    scope.launch {
        val futureTarget =
            Glide.with(this@showNotificationUserOnline)
                .asBitmap()
                .load(photoUrl)
                .submit()
                .get()

        withContext(Dispatchers.Main) {
            notification.setLargeIcon(futureTarget)
            NotificationManagerCompat.from(this@showNotificationUserOnline)
                .notify(NOTIFICATION_ID, notification.build())
        }
    }
}
