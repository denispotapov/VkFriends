package com.example.vkmessenger.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkmessenger.VkRepository
import com.example.vkmessenger.network.ResponseResultUser2
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(private val vkRepository: VkRepository) :
    ViewModel() {

    val userInfo = vkRepository.geUserInfo()

    fun deleteAllFriends() = viewModelScope.launch {
        vkRepository.deleteAllFriends()
    }

    fun deleteUser() = viewModelScope.launch {
        vkRepository.deleteUser()
    }


    fun requestUser(user: ResponseResultUser2) = viewModelScope.launch {
        vkRepository.requestUser(user)
    }
}