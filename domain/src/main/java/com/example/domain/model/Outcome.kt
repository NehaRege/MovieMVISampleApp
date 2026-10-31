package com.example.domain.model

sealed interface Outcome<out T> {
    data class Success<out T>(val value: T) : Outcome<T>
    data class Failure(val error: DomainError) : Outcome<Nothing>
}