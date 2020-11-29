package com.example.vkmessenger.network.getuserinfo

import com.example.vkmessenger.BaseVkResponse
import com.example.vkmessenger.network.VkErrorResponse

data class ResponseResultUser(
    val response: List<ResponseUser>?,
    override val error: VkErrorResponse?
) : BaseVkResponse()