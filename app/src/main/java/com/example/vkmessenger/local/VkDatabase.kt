package com.example.vkmessenger.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Friend::class], version = 1, exportSchema = false)
abstract class VkDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun friendsDao(): FriendsDao
}