package com.example.vkmessenger.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Query("SELECT * FROM friends_table")
    fun getAllFriends(): Flow<List<Friend>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFriends(users: List<Friend>)

    @Query("DELETE FROM friends_table")
    suspend fun deleteAllFriends()

    @Query("SELECT * FROM friends_table WHERE id IN(:onlineIds)")
    suspend fun getOnlineFriends(onlineIds: List<Int>): List<Friend>
}