package com.example.vkmessenger.network

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class VkRetrofitDataSource @Inject constructor(
    private val vkApi: VkApi,
    private val ioDispatcher: CoroutineDispatcher,
    private val accessToken: Provider<String>
) : VkNetworkDataSource {
    override suspend fun getFriendsFromVK(friendsUser: ResponseResultFriends) =
        withContext(ioDispatcher) {
            try {
                val response = vkApi.getFriends(accessToken.get())
                val friendsResponse = response.body()
                return@withContext if (response.isSuccessful && friendsResponse != null) {
                    Log.d("Token", "getFriendsFromVK: $accessToken")
                    Result.Success(friendsResponse)
                } else {
                    Result.Error(Exception())
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getUserInfo(user: ResponseResultUser2): Result<ResponseResultUser2> =
        withContext(ioDispatcher) {
            try {
                val response = vkApi.getUserInfo(accessToken.get())
                val userResponse = response.body()
                return@withContext if (response.isSuccessful && userResponse != null) {
                    Result.Success(userResponse)
                } else {
                    Result.Error(Exception())
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
