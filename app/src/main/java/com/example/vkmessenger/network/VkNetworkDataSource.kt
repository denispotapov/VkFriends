package com.example.vkmessenger.network

interface VkNetworkDataSource {

    suspend fun getFriendsFromVK(friendsUser: ResponseResultFriends): Result<ResponseResultFriends>

    suspend fun getUserInfo(user: ResponseResultUser2): Result<ResponseResultUser2>
}