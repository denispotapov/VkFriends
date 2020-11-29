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

    private val _message: MutableLiveData<String> = MutableLiveData("")
    val message: LiveData<String> = _message

    var result = MutableLiveData<Boolean>()

    fun requestFriends() {
        viewModelScope.launch {
            when( val requestFriends = vkRepository.requestAllFriends()) {
                is Result.Error -> {
                    _message.value = requestFriends.getString()
                    result.value = false
                }
            }
        }
    }
}












