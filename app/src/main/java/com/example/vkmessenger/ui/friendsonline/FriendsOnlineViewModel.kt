package com.example.vkmessenger.ui.friendsonline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FriendsOnlineViewModel @Inject constructor(private val vkRepository: VkRepository) :
    ViewModel() {

    private val friends = vkRepository.getAllFriends()

    val allFriends: LiveData<List<Friend>> = friends.asLiveData()

    private val _message: MutableLiveData<String> = MutableLiveData("")
    val message: LiveData<String> = _message

    val onlineFriends: LiveData<List<Friend>> = friends.map { friendsList ->
        when (val onlineIdsResult = vkRepository.getOnlineFriendsIds()) {
            is Result.Success -> {
                friendsList.filter { friend -> friend.id in onlineIdsResult.data }
            }
            is Result.Error -> {
                _message.value = onlineIdsResult.exception.toString()
                emptyList()
            }
        }
    }.asLiveData()
}