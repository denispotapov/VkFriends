package com.example.vkmessenger

import com.example.vkmessenger.network.Result

interface FriendsRepository {

    suspend fun requestFriends(friends: ResultFriends): Result<Unit>
}