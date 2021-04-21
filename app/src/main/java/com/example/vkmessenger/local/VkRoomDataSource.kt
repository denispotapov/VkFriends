package com.example.vkmessenger.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VkRoomDataSource (
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

    override suspend fun insertAllFriends(
        id: Int,
        firstName: String,
        lastName: String,
        photo: String
    ) = withContext(ioDispatcher) {
        friendsDao.insertAllFriends(id, firstName, lastName, photo)
    }
    

    override suspend fun updateFriend(friend: Friend) = withContext(ioDispatcher) {
        friendsDao.updateFriend(friend)
    }


    override suspend fun deleteAllFriends() = withContext(ioDispatcher) {
        friendsDao.deleteAllFriends()
    }

    override suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend> =
        withContext(ioDispatcher) {
            friendsDao.getOnlineFriends(onlineIds)
        }
}
