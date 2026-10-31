package com.example.core.error

import androidx.annotation.StringRes
import com.example.core.R
import com.example.domain.model.DomainError

@StringRes
fun DomainError.toMessageRes(): Int = when (this) {
    DomainError.Network -> R.string.error_network
    DomainError.Unauthorized -> R.string.error_unauthorized
    DomainError.NotFound -> R.string.error_not_found
    DomainError.Server -> R.string.error_server
    DomainError.Unknown -> R.string.error_unknown
}