package com.example.vkmessenger.network

import com.example.vkmessenger.network.getfriendsfromvk.ResponseResultFriends
import com.example.vkmessenger.network.getfriendsonlineid.ResponseFriendsOnlineIds
import com.example.vkmessenger.network.getuserinfo.ResponseResultUser
import com.example.vkmessenger.util.API_VERSION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VkRetrofitApi {

    @GET("users.get?fields=photo_100&v=$API_VERSION")
    suspend fun getUserInfo(@Query("access_token") accessToken: String): Response<ResponseResultUser>

    @GET("friends.get?fields=photo_100&v=$API_VERSION") //todo move query fields to query parameters
    suspend fun getFriends(@Query("access_token") accessToken: String): Response<ResponseResultFriends>

    @GET("friends.getOnline?v=$API_VERSION")
    suspend fun getFriendsOnlineIds(@Query("access_token") accessToken: String): Response<ResponseFriendsOnlineIds>

}