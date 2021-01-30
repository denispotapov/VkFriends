package com.example.vkmessenger.network

import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: CustomException) : Result<Nothing>()

    fun getString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> when {
                !exception.message.isNullOrBlank() -> {
                    exception.message
                }
                exception.cause is ConnectException || exception.cause is TimeoutException || exception.cause is UnknownHostException -> {
                    "Ошибка соединения: отсутствует подключение к сети или сервер недоступен"
                }
                else -> {
                    "Что-то пошло не так, попробуйте повторить позднее"
                }
            }
        }
    }
}

data class CustomException(
    override val message: String? = null,
    override val cause: Throwable? = null,
    val vkErrorCode: Int? = null
) : Exception(message, cause)