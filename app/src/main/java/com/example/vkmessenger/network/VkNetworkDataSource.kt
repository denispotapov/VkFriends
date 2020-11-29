package com.example.vkmessenger.network

import com.example.vkmessenger.network.getfriendsfromvk.ResponseResultFriends
import com.example.vkmessenger.network.getuserinfo.ResponseResultUser
import com.example.vkmessenger.util.API_VERSION
import com.example.vkmessenger.util.FIELDS

interface VkNetworkDataSource {

    suspend fun getUserInfo(fields: String, apiVersion: String): Result<ResponseResultUser>

    suspend fun getFriendsFromVK(fields: String, apiVersion: String): Result<ResponseResultFriends>

    suspend fun getFriendsOnlineIds(apiVersion: String): Result<List<Int>>
}