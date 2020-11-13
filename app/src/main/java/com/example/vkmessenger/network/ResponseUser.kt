package com.example.vkmessenger.network

import com.example.vkmessenger.local.UserInfo

data class ResponseUser(var id: Int?, val first_name: String?, val last_name: String?, val photo_100: String?) {
    fun toEntity() = UserInfo(id ?: -1, first_name.orEmpty(), last_name.orEmpty(), photo_100.orEmpty())
}