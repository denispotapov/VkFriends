package com.example.vkmessenger.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FriendsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserInfo>)

    @Query("DELETE FROM user_table")
    suspend fun deleteAllFriends()


}