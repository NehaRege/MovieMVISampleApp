package com.example.core.error

sealed interface AppError {
    data object NetworkError : AppError
    data object UnknownError : AppError
    data object UnauthorizedError : AppError
    data object NotFoundError : AppError
    data object ServerError : AppError
}