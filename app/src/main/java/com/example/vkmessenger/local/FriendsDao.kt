package com.example.vkmessenger.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Query("SELECT * FROM user_table")
    fun getAllTasks(): Flow<List<UserInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserInfo>)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllFriends()
}