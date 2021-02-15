package com.example.vkmessenger.ui.friends

import androidx.lifecycle.*
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val vkRepository: VkRepository) : ViewModel() {

    private val friends = vkRepository.getAllFriends()

    private val allFriends: LiveData<List<Friend>> = friends.asLiveData()
    private val filteredFriends = MutableLiveData<List<Friend>>()

    private val _resultFilteredFriends = MediatorLiveData<List<Friend>>()
    val resultFilteredFriends: LiveData<List<Friend>> = _resultFilteredFriends

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        _resultFilteredFriends.addSource(allFriends) {
            filteredFriends.value = it
        }

        _resultFilteredFriends.addSource(filteredFriends) {
            _resultFilteredFriends.value = it
        }
    }

    val loading = MutableLiveData(false)

    fun filterFriends(query: String) {
        viewModelScope.launch {
            val filteredList = mutableListOf<Friend>()
            allFriends.value?.map {
                if (it.firstName.contains(query, true) || it.lastName.contains(query, true)) {
                    filteredList.add(it)
                }
            }
            filteredFriends.value = filteredList
        }
    }

    fun requestFriends() {
        viewModelScope.launch {
            when (val requestFriends = vkRepository.requestAllFriends()) {
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












