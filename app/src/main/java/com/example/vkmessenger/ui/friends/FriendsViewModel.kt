package com.example.vkmessenger.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkmessenger.FriendsRepository
import com.example.vkmessenger.ResultFriends
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val friendsRepository: FriendsRepository) : ViewModel() {

    fun requestFriends(friends: ResultFriends) = viewModelScope.launch {
        friendsRepository.requestFriends(friends)
    }
}