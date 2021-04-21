package com.example.vkmessenger.network

import com.example.vkmessenger.network.getfriendsfromvk.ResponseResultFriends
import com.example.vkmessenger.network.getfriendsonlineid.ResponseFriendsOnlineIds
import com.example.vkmessenger.network.getuserinfo.ResponseResultUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VkRetrofitApi {

    @GET("users.get")
    suspend fun getUserInfo(
        @Query("fields") fields: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<ResponseResultUser>

    @GET("friends.get")
    suspend fun getFriends(
        @Query("fields") fields: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<ResponseResultFriends>

    @GET("friends.getOnline?")
    suspend fun getFriendsOnlineIds(
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<ResponseFriendsOnlineIds>
}