package com.example.vkmessenger.network

interface FriendsFromVK {
    suspend fun getFriendsFromVK(friendsUser: ResponseResultFriends): Result<ResponseResultFriends>
}