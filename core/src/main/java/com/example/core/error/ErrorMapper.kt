package com.example.core.error

import com.example.core.error.AppError.NetworkError
import com.example.core.error.AppError.NotFoundError
import com.example.core.error.AppError.ServerError
import com.example.core.error.AppError.UnauthorizedError
import com.example.core.error.AppError.UnknownError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toAppError(): AppError =
    when (this) {
        is IOException -> NetworkError
        is HttpException -> when (code()) {
            401 -> UnauthorizedError
            404 -> NotFoundError
            in 500..599 -> ServerError
            else -> UnknownError
        }

        else -> UnknownError
    }

fun AppError.toErrorMessage(): String = when (this) {
    NetworkError -> "No internet connection. Please try again."
    UnauthorizedError -> "Authentication failed."
    ServerError -> "Server error. Please try again later."
    NotFoundError -> "Not found."
    UnknownError -> "Something went wrong."
}

