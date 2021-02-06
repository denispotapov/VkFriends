package com.example.vkmessenger.ui.friends

import androidx.lifecycle.*
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val vkRepository: VkRepository) : ViewModel() {

    private val friends = vkRepository.getAllFriends()

    val allFriends: LiveData<List<Friend>> = friends.asLiveData()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    val loading = MutableLiveData(false)

    fun requestFriends() {
        viewModelScope.launch {
            when(val requestFriends = vkRepository.requestAllFriends()) {
                is Result.Success -> loading.value = true
                is Result.Error -> {
                    _message.value = requestFriends.getString()
                }
            }
        }
    }

    fun updateFriend(friend: Friend) {
        viewModelScope.launch {
            vkRepository.updateFriend(friend)
        }
    }
}












