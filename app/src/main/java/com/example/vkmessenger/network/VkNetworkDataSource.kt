package com.example.vkmessenger.network

import com.example.vkmessenger.network.getfriendsfromvk.ResponseResultFriends
import com.example.vkmessenger.network.getuserinfo.ResponseResultUser

interface VkNetworkDataSource {

    suspend fun getUserInfo(fields: String, apiVersion: String, accessToken: String): Result<ResponseResultUser>

    suspend fun getFriendsFromVK(fields: String, apiVersion: String, accessToken: String): Result<ResponseResultFriends>

    suspend fun getFriendsOnlineIds(apiVersion: String, accessToken: String): Result<List<Int>>
}