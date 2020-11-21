package com.example.vkmessenger.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.local.User
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(private val vkRepository: VkRepository) :
    ViewModel() {

    val userInfo: LiveData<User> = vkRepository.geUserInfo().asLiveData()

    fun onAccessTokenObtained() {
        requestUser()
    }

    fun onExitItemSelected() {
        deleteAllFriends()
    }

    private fun requestUser() {
        viewModelScope.launch { vkRepository.requestUser() }
    }

    private fun deleteUser() {
        viewModelScope.launch { vkRepository.deleteUser() }
    }

    private fun deleteAllFriends() {
        viewModelScope.launch { vkRepository.deleteAllFriends() }
    }
}