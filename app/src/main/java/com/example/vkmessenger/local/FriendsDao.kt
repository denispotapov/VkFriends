package com.example.vkmessenger.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Query("SELECT * FROM friends_table ORDER by tracking DESC")
    fun getAllFriends(): Flow<List<Friend>>

    suspend fun insertAllFriends(
        id: Int,
        firstName: String,
        lastName: String,
        photo: String
    ) {
        updateFriends(id, firstName, lastName, photo)
        insertFriends(id, firstName, lastName, photo)
    }

    @Update
    suspend fun updateFriend(friend: Friend)

    @Query("DELETE FROM friends_table")
    suspend fun deleteAllFriends()

    @Query("SELECT * FROM friends_table WHERE id IN (:onlineIds) ORDER by tracking DESC")
    suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend>

    @Query // if no update happened (i.e. the row didn't exist) then insert one
        ("INSERT INTO friends_table (id, first_name, last_name, photo) SELECT :id, :firstName, :lastName, :photo WHERE (Select Changes() = 0)")
    suspend fun insertFriends(
        id: Int,
        firstName: String,
        lastName: String,
        photo: String
    )

    @Query("UPDATE friends_table SET first_name =:firstName, last_name =:lastName, photo =:photo WHERE id =:id")
    suspend fun updateFriends(
        id: Int,
        firstName: String,
        lastName: String,
        photo: String
    )
}