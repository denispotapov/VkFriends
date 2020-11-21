package com.example.vkmessenger.network

import com.example.vkmessenger.network.getfriendsfromvk.ResponseResultFriends
import com.example.vkmessenger.network.getuserinfo.ResponseResultUser

interface VkNetworkDataSource {

    suspend fun getUserInfo(): Result<ResponseResultUser>

    suspend fun getFriendsFromVK(): Result<ResponseResultFriends>

    suspend fun getFriendsOnlineIds(): Result<List<Int>>
}