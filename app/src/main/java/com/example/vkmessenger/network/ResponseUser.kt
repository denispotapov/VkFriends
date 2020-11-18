package com.example.vkmessenger.network

import com.example.vkmessenger.local.User

data class ResponseUser(
    val first_name: String?,
    val last_name: String?,
    val photo_100: String?
) {
    fun toEntity() = User(first_name.orEmpty(), last_name.orEmpty(), photo_100.orEmpty())
}