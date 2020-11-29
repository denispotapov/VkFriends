package com.example.vkmessenger.ui.authorization

import androidx.lifecycle.*
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.User
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(private val vkRepository: VkRepository) :
    ViewModel() {

    val userInfo: LiveData<User> = vkRepository.geUserInfo().asLiveData()

    private val _message: MutableLiveData<String> = MutableLiveData("")
    val message: LiveData<String> = _message

    var result = MutableLiveData<Boolean>()

    fun onAccessTokenObtained() {
        requestUser()
    }

    fun onExitItemSelected() {
        deleteAllFriends()
    }

    private fun requestUser() {
        viewModelScope.launch {
            when (val requestUser = vkRepository.requestUser()) {
                is Result.Error -> {
                    _message.value = requestUser.getString()
                    result.value = false
                }
            }
        }
    }

    private fun deleteUser() {
        viewModelScope.launch { vkRepository.deleteUser() }
    }

    private fun deleteAllFriends() {
        viewModelScope.launch { vkRepository.deleteAllFriends() }
    }
}