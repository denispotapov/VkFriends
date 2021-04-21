package com.example.vkmessenger

import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.local.User
import com.example.vkmessenger.network.Result
import com.example.vkmessenger.util.API_VERSION
import com.example.vkmessenger.util.Credentials
import com.example.vkmessenger.util.FIELDS
import kotlinx.coroutines.flow.Flow

interface VkRepository {

    fun geUserInfo(): Flow<User>

    suspend fun requestUser1(fields: String = FIELDS, apiVersion: String = API_VERSION, accessToken: String = Credentials.accessToken): Result<Unit>

    suspend fun deleteUser()

    fun getAllFriends(): Flow<List<Friend>>

    suspend fun requestAllFriends(fields: String = FIELDS, apiVersion: String = API_VERSION, accessToken: String = Credentials.accessToken): Result<Unit>

    suspend fun updateFriend(friend: Friend)

    suspend fun deleteAllFriends()

    suspend fun getOnlineFriendsIds(apiVersion: String = API_VERSION, accessToken: String = Credentials.accessToken): Result<List<Int>>

    suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend>
}