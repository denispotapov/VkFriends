package com.example.vkmessenger.local

import kotlinx.coroutines.flow.Flow

interface FriendsLocalDataSource {

    fun getFriends(): Flow<List<UserInfo>>

    suspend fun deleteAllFriends()

    suspend fun insertAll(users: List<UserInfo>)
}