package com.example.vkmessenger.network

import com.example.vkmessenger.network.getfriendsfromvk.ResponseResultFriends
import com.example.vkmessenger.network.getuserinfo.ResponseResultUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class VkRetrofitDataSource @Inject constructor(
    private val vkRetrofitApi: VkRetrofitApi,
    private val ioDispatcher: CoroutineDispatcher,
    private val accessToken: Provider<String> //todo add named annotation
) : VkNetworkDataSource {

    override suspend fun getUserInfo(): Result<ResponseResultUser> =
        withContext(ioDispatcher) {
            try {
                val response = vkRetrofitApi.getUserInfo(accessToken.get())
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

    override suspend fun getFriendsFromVK(): Result<ResponseResultFriends> =
        withContext(ioDispatcher) {
            try {
                val response = vkRetrofitApi.getFriends(accessToken.get())
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

    override suspend fun getFriendsOnlineIds(): Result<List<Int>> =
        withContext(ioDispatcher) {
            try {
                val response = vkRetrofitApi.getFriendsOnlineIds(accessToken.get())
                val listOfIds = response.body()?.response
                return@withContext if (response.isSuccessful && listOfIds != null) {
                    Result.Success(listOfIds)
                } else {
                    Result.Error(Exception())
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
