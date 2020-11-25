package com.example.vkmessenger.local

import kotlinx.coroutines.flow.Flow

interface VkLocalDataSource {

    fun getUserInfo(): Flow<User>

    suspend fun insertUser(user: User)

    suspend fun deleteUser()

    fun getAllFriends(): Flow<List<Friend>>

    suspend fun insertAllFriends(users: List<Friend>)

    suspend fun deleteAllFriends()

    suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend>
}