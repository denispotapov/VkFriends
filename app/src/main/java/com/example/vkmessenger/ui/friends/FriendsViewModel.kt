package com.example.vkmessenger.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val vkRepository: VkRepository) : ViewModel() {

    private val friends = vkRepository.getAllFriends()

    val allFriends: LiveData<List<Friend>> = friends.asLiveData()

    fun requestFriends() {
        viewModelScope.launch {
            vkRepository.requestAllFriends()
        }
    }
}












