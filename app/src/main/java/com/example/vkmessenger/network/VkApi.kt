package com.example.vkmessenger.network

import com.example.vkmessenger.ResultUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {

    @GET("users.get?fields=photo_100&v=5.52&access_token=access_token")
    fun getUserInfo(@Query("access_token") access_token: String): Call<ResultUser>

    /*@GET("friends.getOnline?v=5.52&access_token=$token")
    fun getFriendsOnline(): Call<ResultFriendsOnline>*/

    @GET("friends.get?fields=photo_100&v=5.52&access_token=access_token")
    suspend fun getFriends(@Query("access_token") access_token: String): Response<ResponseResultFriends>


}