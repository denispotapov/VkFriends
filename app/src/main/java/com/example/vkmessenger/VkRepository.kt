package com.example.vkmessenger

import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.local.User
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.flow.Flow

interface VkRepository {

    fun geUserInfo(): Flow<User>

    suspend fun requestUser(): Result<Unit>

    suspend fun deleteUser()

    fun getAllFriends(): Flow<List<Friend>>

    suspend fun requestAllFriends(): Result<Unit> // todo read about kotlin default arguments

    suspend fun deleteAllFriends()

    suspend fun getOnlineFriendsIds(): Result<List<Int>>

    suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend>
}

// apiV: String = API_VERSION