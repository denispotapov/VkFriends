package com.example.vkmessenger.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VkRoomDataSource @Inject constructor(
    private val friendsDao: FriendsDao,
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher
) : VkLocalDataSource {

    override fun getUserInfo(): Flow<User> = userDao.getUserInfo()

    override suspend fun insertUser(user: User) = withContext(ioDispatcher) {
        userDao.insertUser(user)
    }

    override suspend fun deleteUser() = withContext(ioDispatcher) {
        userDao.deleteUser()
    }

    override fun getAllFriends(): Flow<List<Friend>> = friendsDao.getAllFriends()

    override suspend fun insertAllFriends(users: List<Friend>) = withContext(ioDispatcher) {
        friendsDao.insertAllFriends(users)
    }

    override suspend fun deleteAllFriends() = withContext(ioDispatcher) {
        friendsDao.deleteAllFriends()
    }
}
