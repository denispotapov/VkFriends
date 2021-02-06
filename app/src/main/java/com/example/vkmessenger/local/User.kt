package com.example.vkmessenger.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vkmessenger.util.AUTHORIZED_USER_ID

@Entity(tableName = "user")
data class User(
    val firstName: String,
    val lastName: String,
    val photo: String,
) {

    @PrimaryKey
    var id: Int = AUTHORIZED_USER_ID
}