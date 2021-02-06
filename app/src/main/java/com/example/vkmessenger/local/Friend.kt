package com.example.vkmessenger.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends_table")
data class Friend(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false) var id: Int,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "photo")
    val photo: String

) {
    var tracking: Boolean? = null

    constructor(
        id: Int,
        firstName: String,
        lastName: String,
        photo: String,
        tracking: Boolean?
    ) : this(id, firstName, lastName, photo) {
        this.tracking = tracking
    }
}