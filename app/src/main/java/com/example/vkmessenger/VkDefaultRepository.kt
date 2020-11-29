package com.example.vkmessenger

import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.local.User
import com.example.vkmessenger.local.VkLocalDataSource
import com.example.vkmessenger.network.Result
import com.example.vkmessenger.network.VkNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class VkDefaultRepository @Inject constructor(
    private val vkLocalDataSource: VkLocalDataSource,
    private val vkNetworkDataSource: VkNetworkDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : VkRepository {

    override fun geUserInfo(): Flow<User> = vkLocalDataSource.getUserInfo()
        .flowOn(ioDispatcher)

    override suspend fun requestUser(fields: String,
                                     apiVersion: String): Result<Unit> =
        withContext(ioDispatcher) {
            when (val getUserResult = vkNetworkDataSource.getUserInfo(fields, apiVersion)) {
                is Result.Success -> {
                    val user = getUserResult.data.response?.firstOrNull()?.toEntity()
                    user?.let { vkLocalDataSource.insertUser(it) }
                    Timber.d(
                        "Repository: ${
                            getUserResult.data.response?.map { it.toEntity() }?.get(0)
                        }"
                    )
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    Timber.d("requestUser: Error")
                    Result.Error(getUserResult.exception) // todo add UI exception handling
                }
            }
        }

    override suspend fun deleteUser() = withContext(ioDispatcher) {
        vkLocalDataSource.deleteUser()
    }

    override fun getAllFriends(): Flow<List<Friend>> =
        vkLocalDataSource.getAllFriends().flowOn(ioDispatcher)

    override suspend fun requestAllFriends(fields: String,
                                           apiVersion: String): Result<Unit> =
        withContext(ioDispatcher) {
            when (val getFriendsResult = vkNetworkDataSource.getFriendsFromVK(fields,
                apiVersion)) {
                is Result.Success -> {
                    Timber.d("requestFriends: ${getFriendsResult.data}")
                    val friends = getFriendsResult.data.response?.items?.map { it.toEntity() }
                    friends?.let { vkLocalDataSource.insertAllFriends(it) }
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    Timber.d("requestFriends: Error")
                    Result.Error(getFriendsResult.exception)
                }
            }
        }

    override suspend fun deleteAllFriends() = withContext(ioDispatcher) {
        vkLocalDataSource.deleteAllFriends()
    }

    override suspend fun getOnlineFriendsIds(apiVersion: String): Result<List<Int>> = withContext(ioDispatcher) {
        when (val getFriendsResultIds = vkNetworkDataSource.getFriendsOnlineIds(apiVersion)) {
            is Result.Success -> {
                val onlineIds = getFriendsResultIds.data
                Timber.d("Список онлайн ids: $onlineIds")
                Result.Success(onlineIds)
            }
            is Result.Error -> Result.Error(getFriendsResultIds.exception)
        }
    }

    override suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend> =
        withContext(ioDispatcher) {
            vkLocalDataSource.getOnlineFriends(onlineIds)
        }
}



