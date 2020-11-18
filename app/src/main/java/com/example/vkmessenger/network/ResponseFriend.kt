package com.example.vkmessenger.network

import com.example.vkmessenger.local.Friend

data class ResponseFriend(
    var id: Int?,
    val first_name: String?,
    val last_name: String?,
    val photo_100: String?
) {
    fun toEntity() =
        Friend(id ?: -1, first_name.orEmpty(), last_name.orEmpty(), photo_100.orEmpty())
}