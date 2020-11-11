package com.example.vkmessenger

import android.util.Log
import com.example.vkmessenger.network.FriendsFromVK
import com.example.vkmessenger.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FriendsDefaultRepository @Inject constructor(
    private val
    friendsFromVK: FriendsFromVK,
    private val ioDispatcher: CoroutineDispatcher
) : FriendsRepository {
    override suspend fun requestFriends(friends: ResultFriends): Result<Unit> =
        withContext(ioDispatcher) {
            when (val getFriendsResult = friendsFromVK.getFriendsFromVK(friends)) {
                is Result.Success -> {
                    Log.d("Repository", "requestFriends: ${getFriendsResult.data}")
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    Log.d("Repository", "requestFriends: Error")
                    Result.Error(getFriendsResult.exception)
                }
            }
        }
}

