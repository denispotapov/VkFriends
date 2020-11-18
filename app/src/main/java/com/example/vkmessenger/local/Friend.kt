package com.example.vkmessenger.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends_table")
data class Friend(
    @PrimaryKey(autoGenerate = false) var id: Int,
    val first_name: String,
    val last_name: String,
    val photo_100: String
)