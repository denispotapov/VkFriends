package com.example.vkmessenger.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.Result
import com.example.vkmessenger.util.showNotificationUserOnline
import com.google.gson.Gson
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class StatusTrackingService : JobService() {

    @Inject
    lateinit var interactor: StatusTrackingServiceInteractor

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        val json = params?.extras?.getString("friend")
        val gSon = Gson()
        val friend = gSon.fromJson(json, Friend::class.java)

        interactor.trackUserOnlineStatus(this, friend)

        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean = false
}