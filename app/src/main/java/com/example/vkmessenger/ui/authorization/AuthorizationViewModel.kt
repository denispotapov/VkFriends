package com.example.vkmessenger.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkmessenger.FriendsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(private val friendsRepository: FriendsRepository) :
    ViewModel() {

    fun deleteAllFriends() = viewModelScope.launch {
        friendsRepository.deleteAllFriends()
    }
}