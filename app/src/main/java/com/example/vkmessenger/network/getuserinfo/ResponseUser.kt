package com.example.vkmessenger.network.getuserinfo

import com.example.vkmessenger.local.User
import com.google.gson.annotations.SerializedName

data class ResponseUser(
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("photo_100")
    val photo: String?
) {
    fun toEntity() = User(firstName.orEmpty(), lastName.orEmpty(), photo.orEmpty())
}