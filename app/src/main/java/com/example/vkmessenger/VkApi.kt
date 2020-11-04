package com.example.vkmessenger

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {

    @GET("users.get?fields=photo_50&v=5.52&access_token=access_token")
    fun getUserInfo(@Query("access_token") access_token: String): Call<ResultUser>

    /*@GET("friends.getOnline?v=5.52&access_token=$token")
    fun getFriendsOnline(): Call<ResultFriendsOnline>

    @GET("friends.get?v=5.52&access_token=$token")
    fun getFriends(): Call<ResultFriends>*/


}