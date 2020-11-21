package com.example.vkmessenger.ui.friends

import androidx.lifecycle.*
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val vkRepository: VkRepository) : ViewModel() {

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

    fun requestFriends() {
        viewModelScope.launch {
            vkRepository.requestAllFriends()
        }
    }
}












