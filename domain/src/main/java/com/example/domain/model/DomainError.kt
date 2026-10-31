package com.example.domain.model

/*
Why data object and not plain object?
Both create a single instance.
data object also gives you a readable toString(): printing it shows Network, while a plain object prints something like DomainError$Network@1b6d3586.
That matters in logs, test failures and debugging.
 */
sealed interface DomainError {
    data object Network : DomainError
    data object Unauthorized : DomainError
    data object NotFound : DomainError
    data object Server : DomainError
    data object Unknown : DomainError
}