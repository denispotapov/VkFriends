package com.example.vkmessenger.ui.authorization

import androidx.lifecycle.*
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.User
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.launch

class AuthorizationViewModel(private val vkRepository: VkRepository) :
    ViewModel() {

    val userInfo: LiveData<User> = vkRepository.geUserInfo().asLiveData()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun onAccessTokenObtained() {
        requestUser1()
    }

    fun onExitItemSelected() {
        deleteAllFriends()
    }

    /*private fun requestUser() {
        viewModelScope.launch {
            when (val requestUser = vkRepository.requestUser()) {
                is Result.Error -> {
                    _message.value = requestUser.getString()
                }
            }
        }
    }*/

    private fun requestUser1() {
        viewModelScope.launch {
            when (val requestUser = vkRepository.requestUser1()) {
                is Result.Error -> {
                    _message.value = requestUser.getString()
                }
            }
        }
    }

    private fun deleteAllFriends() {
        viewModelScope.launch { vkRepository.deleteAllFriends() }
    }

    private fun deleteUser() {
        viewModelScope.launch { vkRepository.deleteUser() }
    }
}