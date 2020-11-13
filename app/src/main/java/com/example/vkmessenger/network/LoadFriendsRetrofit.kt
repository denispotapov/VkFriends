package com.example.vkmessenger.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadFriendsRetrofit @Inject constructor(
    private val vkApi: VkApi,
    private val ioDispatcher: CoroutineDispatcher,
    private val accessToken: String
) : FriendsFromVK {
    override suspend fun getFriendsFromVK(friendsUser: ResponseResultFriends) =
        withContext(ioDispatcher) {
            try {
                val response = vkApi.getFriends(accessToken)
                val friendsResponse = response.body()
                return@withContext if (response.isSuccessful && friendsResponse != null) {
                    Result.Success(friendsResponse)
                } else {
                    Result.Error(Exception())
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

}
