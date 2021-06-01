package com.example.vkmessenger.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatusTrackingServiceInteractor(private val vkRepository: VkRepository) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _trackingFriendsOnline = MutableLiveData<List<Friend>>()
    val trackingFriendsOnline: LiveData<List<Friend>> = _trackingFriendsOnline

    fun getTrackingFriendsOnline() {
        scope.launch {
            when (val onlineIdsResult = vkRepository.getOnlineFriendsIds()) {
                is Result.Success -> {
                    val listOnlineFriends = vkRepository.getOnlineFriends(onlineIdsResult.data)
                    _trackingFriendsOnline.postValue(listOnlineFriends.filter {
                        it.tracking == true
                    })
                }
            }
        }
    }
}