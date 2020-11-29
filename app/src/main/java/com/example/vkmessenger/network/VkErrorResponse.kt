package com.example.vkmessenger.network

import com.google.gson.annotations.SerializedName

data class VkErrorResponse(
    @SerializedName("error_code")
    val errorCode: Int?,
    @SerializedName("error_msg")
    val errorMessage: String?
)