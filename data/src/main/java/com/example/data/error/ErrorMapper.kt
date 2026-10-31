package com.example.data.error

import com.example.domain.model.DomainError
import com.example.domain.model.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

internal fun Throwable.toDomainError(): DomainError = when (this) {
    is IOException -> DomainError.Network
    is HttpException -> when (code()) {
        401 -> DomainError.Unauthorized
        404 -> DomainError.NotFound
        in 500..599 -> DomainError.Server
        else -> DomainError.Unknown
    }
    else -> DomainError.Unknown
}

internal fun <T> Flow<T>.asOutcome(): Flow<Outcome<T>> =
    map<T, Outcome<T>> { Outcome.Success(it) }
        .catch { throwable -> emit(Outcome.Failure(throwable.toDomainError())) }