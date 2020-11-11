package com.example.vkmessenger.network

import com.example.vkmessenger.ResultFriends

interface FriendsFromVK {
    suspend fun getFriendsFromVK(friendsUser: ResultFriends): Result<ResultFriends>
}