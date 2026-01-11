package com.example.feature_detail_presentation

import com.example.core.error.AppError
import com.example.domain.model.Movie

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movieDetail: Movie? = null,
    val errorMessage: AppError? = null
)
