package com.example.core.error

import com.example.core.error.AppError.NetworkError
import com.example.core.error.AppError.NotFoundError
import com.example.core.error.AppError.ServerError
import com.example.core.error.AppError.Unauthorized
import com.example.core.error.AppError.UnknownError
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toAppError(): AppError =
    when (this) {
        is IOException -> NetworkError
        is HttpException -> when (code()) {
            401 -> Unauthorized
            404 -> NotFoundError
            in 500..599 -> ServerError
            else -> UnknownError
        }

        else -> UnknownError
    }

