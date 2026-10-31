package com.example.feature_detail_presentation

import com.example.domain.model.DomainError
import com.example.domain.model.Movie

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movieDetail: Movie? = null,
    val error: DomainError? = null
)
