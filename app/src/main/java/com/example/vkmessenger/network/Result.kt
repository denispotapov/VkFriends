package com.example.vkmessenger.network

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    class Error(val exception: Exception) : Result<Nothing>()

}