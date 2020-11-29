package com.example.vkmessenger

import com.example.vkmessenger.network.VkErrorResponse

abstract class BaseVkResponse {
    abstract val error: VkErrorResponse?
}