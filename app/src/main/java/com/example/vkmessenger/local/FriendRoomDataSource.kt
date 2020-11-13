package com.example.vkmessenger.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FriendRoomDataSource @Inject constructor(
    private val friendsDao: FriendsDao,
    private val ioDispatcher: CoroutineDispatcher
) : FriendsLocalDataSource {

    override fun getFriends(): Flow<List<UserInfo>> = friendsDao.getAllTasks()

    override suspend fun deleteAllFriends() = withContext(ioDispatcher) {
        friendsDao.deleteAllFriends()
    }
    override suspend fun insertAll(users: List<UserInfo>) = withContext(ioDispatcher) {
        friendsDao.insertAll(users)
    }
}
