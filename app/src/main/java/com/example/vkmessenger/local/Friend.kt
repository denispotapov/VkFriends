package com.example.vkmessenger.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends_table")
data class Friend(
    @PrimaryKey(autoGenerate = false) var id: Int,
    val firstName: String,
    val lastName: String,
    val photo: String
)