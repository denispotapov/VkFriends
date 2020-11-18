package com.example.vkmessenger.local

import kotlinx.coroutines.flow.Flow

interface VkLocalDataSource {

    fun getUserInfo(): Flow<User>

    suspend fun insertUser(user: User)

    suspend fun deleteUser()

    fun getFriends(): Flow<List<Friend>>

    suspend fun deleteAllFriends()

    suspend fun insertAllFriends(users: List<Friend>)
}