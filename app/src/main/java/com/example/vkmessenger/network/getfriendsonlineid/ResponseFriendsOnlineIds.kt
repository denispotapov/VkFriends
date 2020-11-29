package com.example.vkmessenger.network.getfriendsonlineid

import com.example.vkmessenger.BaseVkResponse
import com.example.vkmessenger.network.VkErrorResponse

data class ResponseFriendsOnlineIds(
    val response: List<Int>?,
    override val error: VkErrorResponse?
) : BaseVkResponse()