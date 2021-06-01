package com.example.vkmessenger.network

import com.example.vkmessenger.network.getfriendsfromvk.ResponseResultFriends
import com.example.vkmessenger.network.getuserinfo.ResponseResultUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class VkRetrofitDataSource (
    private val vkRetrofitApi: VkRetrofitApi,
    private val ioDispatcher: CoroutineDispatcher,
) : VkNetworkDataSource {

    override suspend fun getUserInfo(
        fields: String,
        apiVersion: String, accessToken: String
    ): Result<ResponseResultUser> =
        withContext(ioDispatcher) {
            try {
                Timber.d("Token $accessToken")
                val response = vkRetrofitApi.getUserInfo(fields, apiVersion, accessToken)
                val userResponse = response.body()
                Timber.d("Token $userResponse")
                return@withContext if (response.isSuccessful && userResponse != null) {
                    Result.Success(userResponse)
                } else {
                    val vkErrorResponse = response.body()?.error
                    var errorText = vkErrorResponse?.errorMessage
                    if (errorText.isNullOrBlank()) errorText = GET_USER_INFO_ERROR
                    Result.Error(
                        CustomException(
                            message = errorText,
                            vkErrorCode = vkErrorResponse?.errorCode
                        )
                    )
                }
            } catch (t: Throwable) {
                return@withContext Result.Error(CustomException(cause = t))
            }
        }

    override suspend fun getFriendsFromVK(
        fields: String,
        apiVersion: String,
        accessToken: String

    ): Result<ResponseResultFriends> =
        withContext(ioDispatcher) {
            try {
                val response = vkRetrofitApi.getFriends(fields, apiVersion, accessToken)
                val friendsResponse = response.body()
                Timber.d("Token $friendsResponse")
                return@withContext if (response.isSuccessful && friendsResponse != null) {
                    Result.Success(friendsResponse)
                } else {
                    val vkErrorResponse = response.body()?.error
                    var errorText = vkErrorResponse?.errorMessage
                    if (errorText.isNullOrBlank()) errorText = GET_FRIENDS_ERROR
                    Result.Error(
                        CustomException(
                            message = errorText,
                            vkErrorCode = vkErrorResponse?.errorCode
                        )
                    )
                }
            } catch (t: Throwable) {
                return@withContext Result.Error(CustomException(cause = t))
            }

        }

    override suspend fun getFriendsOnlineIds(apiVersion: String, accessToken: String): Result<List<Int>> =
        withContext(ioDispatcher) {
            try {
                val response = vkRetrofitApi.getFriendsOnlineIds(apiVersion, accessToken)
                val listOfIds = response.body()?.response
                return@withContext if (response.isSuccessful && listOfIds != null) {
                    Result.Success(listOfIds)
                } else {
                    val vkErrorResponse = response.body()?.error
                    var errorText = vkErrorResponse?.errorMessage
                    if (errorText.isNullOrBlank()) errorText = GET_FRIENDS_ONLINE_ERROR
                    Result.Error(
                        CustomException(
                            message = errorText,
                            vkErrorCode = vkErrorResponse?.errorCode
                        )
                    )
                }
            } catch (t: Throwable) {
                return@withContext Result.Error(CustomException(cause = t))
            }
        }

    companion object {
        private const val GET_USER_INFO_ERROR = "Не удалось получить информацию о пользователе"
        private const val GET_FRIENDS_ERROR = "Не загрузить друзей"
        private const val GET_FRIENDS_ONLINE_ERROR = "Не загрузить друзей онлайн"
    }
}
