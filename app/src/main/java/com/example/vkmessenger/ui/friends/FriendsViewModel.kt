package com.example.vkmessenger.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.ResponseResultFriends
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val vkRepository: VkRepository) : ViewModel() {

    val allFriends: LiveData<List<Friend>> = vkRepository.getFriends()

    fun requestFriends(friends: ResponseResultFriends) = viewModelScope.launch {
        vkRepository.requestFriends(friends)
    }

}