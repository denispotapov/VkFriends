package com.example.vkmessenger.network.getfriendsfromvk

import com.example.vkmessenger.BaseVkResponse
import com.example.vkmessenger.network.VkErrorResponse

data class ResponseResultFriends(
    val response: ResponseFriends?,
    override val error: VkErrorResponse?
) : BaseVkResponse()