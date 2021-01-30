package com.example.vkmessenger.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.vkmessenger.R
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.Result
import com.example.vkmessenger.ui.friendsonline.FriendsOnlineActivity
import com.example.vkmessenger.util.CHANNEL_ID
import com.example.vkmessenger.util.NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class StatusTrackingServiceInteractor @Inject constructor(private val vkRepository: VkRepository) {

    private val scope = CoroutineScope(Dispatchers.Main)

    fun trackUserOnlineStatus(context: Context, friend: Friend) {
        scope.launch {
            when (val onlineIdsResult = vkRepository.getOnlineFriendsIds()) {
                is Result.Success -> {
                    val onlineIds = onlineIdsResult.data
                    for (i in onlineIds) {
                        if (i == friend.id) {
                            Timber.d("Объект c id: ${friend.id} онлайн")
                            showNotificationUserOnline(context, friend)
                        }
                    }
                }
            }
        }
    }

    private fun showNotificationUserOnline(context: Context, friend: Friend) {
        val scope = CoroutineScope(Dispatchers.IO)

        val openActivityIntent = Intent(context, FriendsOnlineActivity::class.java)
        val openActivityPendingIntent = PendingIntent.getActivity(
            context, 0, openActivityIntent, 0
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Служба отслеживания статуса")
            .setContentText("${friend.firstName} ${friend.lastName} онлайн")
            .setSmallIcon(R.drawable.ic_android_black)
            .setSound(defaultSoundUri)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openActivityPendingIntent)

        scope.launch {
            val futureTarget =
                Glide.with(context)
                    .asBitmap()
                    .load(friend.photo)
                    .submit()
                    .get()

            withContext(Dispatchers.Main) {
                notification.setLargeIcon(futureTarget)
                NotificationManagerCompat.from(context)
                    .notify(NOTIFICATION_ID, notification.build())
            }
        }
    }
}