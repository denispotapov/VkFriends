package com.example.vkmessenger.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(val first_name: String, val last_name: String, val photo_100: String) {

    @PrimaryKey
    var id: Int = 1
}