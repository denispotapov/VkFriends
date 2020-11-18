package com.example.vkmessenger

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.local.User
import com.example.vkmessenger.local.VkLocalDataSource
import com.example.vkmessenger.network.ResponseResultFriends
import com.example.vkmessenger.network.ResponseResultUser2
import com.example.vkmessenger.network.Result
import com.example.vkmessenger.network.VkNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VkDefaultRepository @Inject constructor(
    private val vkLocalDataSource: VkLocalDataSource,
    private val vkNetworkDataSource: VkNetworkDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : VkRepository {

    override fun geUserInfo(): LiveData<User> = vkLocalDataSource.getUserInfo().asLiveData()

    override fun getFriends(): LiveData<List<Friend>> =
        vkLocalDataSource.getFriends().asLiveData()

    override suspend fun deleteUser() = withContext(ioDispatcher) {
        vkLocalDataSource.deleteUser()
    }

    override suspend fun deleteAllFriends() = withContext(ioDispatcher) {
        vkLocalDataSource.deleteAllFriends()
    }

    override suspend fun requestUser(user: ResponseResultUser2): Result<Unit> =
        withContext(ioDispatcher) {
            when (val getUserResult = vkNetworkDataSource.getUserInfo(user)) {
                is Result.Success -> {
                    getUserResult.data.response?.map { it.toEntity() }
                        ?.get(0)?.let { vkLocalDataSource.insertUser(it) }
                    Log.d(
                        "TAG",
                        "Repository: ${getUserResult.data.response?.map { it.toEntity() }?.get(0)}"
                    )
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    Log.d("User", "requestUser: Error")
                    Result.Error(getUserResult.exception)
                }
            }
        }

    override suspend fun requestFriends(friends: ResponseResultFriends): Result<Unit> =
        withContext(ioDispatcher) {
            when (val getFriendsResult = vkNetworkDataSource.getFriendsFromVK(friends)) {
                is Result.Success -> {
                    Log.d("Repository", "requestFriends: ${getFriendsResult.data}")
                    if (getFriendsResult.data.response?.items != null) {
                        vkLocalDataSource.insertAllFriends(getFriendsResult.data.response.items.map { it.toEntity() })
                    }
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    Log.d("Repository", "requestFriends: Error")
                    Result.Error(getFriendsResult.exception)
                }
            }
        }

}

