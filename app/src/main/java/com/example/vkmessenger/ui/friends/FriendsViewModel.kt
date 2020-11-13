package com.example.vkmessenger.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkmessenger.FriendsRepository
import com.example.vkmessenger.local.UserInfo
import com.example.vkmessenger.network.ResponseResultFriends
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val friendsRepository: FriendsRepository) : ViewModel() {

    val allFriends: LiveData<List<UserInfo>> = friendsRepository.getFriends()

    fun requestFriends(friends: ResponseResultFriends) = viewModelScope.launch {
        friendsRepository.requestFriends(friends)
    }
}