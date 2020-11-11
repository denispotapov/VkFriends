package com.example.vkmessenger.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserInfo::class], version = 1, exportSchema = false)
abstract class FriendsDatabase : RoomDatabase() {

    abstract fun friendsDao(): FriendsDao
}