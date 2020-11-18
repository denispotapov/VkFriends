package com.example.vkmessenger

import androidx.lifecycle.LiveData
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.local.User
import com.example.vkmessenger.network.ResponseResultFriends
import com.example.vkmessenger.network.ResponseResultUser2
import com.example.vkmessenger.network.Result

interface VkRepository {

    fun geUserInfo(): LiveData<User>

    fun getFriends(): LiveData<List<Friend>>

    suspend fun deleteUser()

    suspend fun deleteAllFriends()

    suspend fun requestFriends(friends: ResponseResultFriends): Result<Unit>

    suspend fun requestUser(user: ResponseResultUser2): Result<Unit>
}