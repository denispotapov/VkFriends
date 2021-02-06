package com.example.vkmessenger.network.getfriendsfromvk

import com.example.vkmessenger.local.Friend
import com.example.vkmessenger.util.NO_VALUE
import com.google.gson.annotations.SerializedName

data class ResponseFriend(

    var id: Int?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("photo_100")
    val photo: String?
) {

    fun toEntity() = Friend(
        id ?: NO_VALUE, firstName.orEmpty(), lastName.orEmpty(), photo.orEmpty()
    )
}

