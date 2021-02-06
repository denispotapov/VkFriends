package com.example.vkmessenger.local

import kotlinx.coroutines.flow.Flow

interface VkLocalDataSource {

    fun getUserInfo(): Flow<User>

    suspend fun insertUser(user: User)

    suspend fun deleteUser()

    fun getAllFriends(): Flow<List<Friend>>

    suspend fun insertAllFriends(
        id: Int,
        firstName: String,
        lastName: String,
        photo: String
    )

    suspend fun updateFriend(friend: Friend)

    suspend fun deleteAllFriends()

    suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend>
}