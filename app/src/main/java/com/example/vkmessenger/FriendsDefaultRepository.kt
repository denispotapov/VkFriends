package com.example.vkmessenger

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.vkmessenger.local.FriendsLocalDataSource
import com.example.vkmessenger.local.UserInfo
import com.example.vkmessenger.network.FriendsFromVK
import com.example.vkmessenger.network.ResponseResultFriends
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FriendsDefaultRepository @Inject constructor(
    private val friendsLocalDataSource: FriendsLocalDataSource,
    private val friendsFromVK: FriendsFromVK,
    private val ioDispatcher: CoroutineDispatcher
) : FriendsRepository {

    override fun getFriends(): LiveData<List<UserInfo>> =
        friendsLocalDataSource.getFriends().asLiveData()

    override suspend fun deleteAllFriends() = withContext(ioDispatcher) {
        friendsLocalDataSource.deleteAllFriends()
    }

    override suspend fun requestFriends(friends: ResponseResultFriends): Result<Unit> =
        withContext(ioDispatcher) {
            when (val getFriendsResult = friendsFromVK.getFriendsFromVK(friends)) {
                is Result.Success -> {
                    Log.d("Repository", "requestFriends: ${getFriendsResult.data}")
                    if (getFriendsResult.data.response?.items != null) {
                        friendsLocalDataSource.insertAll(getFriendsResult.data.response.items.map { it.toEntity() })
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

