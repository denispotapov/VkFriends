package com.example.vkmessenger

import androidx.lifecycle.LiveData
import com.example.vkmessenger.local.UserInfo
import com.example.vkmessenger.network.ResponseResultFriends
import com.example.vkmessenger.network.Result

interface FriendsRepository {

    fun getFriends(): LiveData<List<UserInfo>>

    suspend fun deleteAllFriends()

    suspend fun requestFriends(friends: ResponseResultFriends): Result<Unit>
}