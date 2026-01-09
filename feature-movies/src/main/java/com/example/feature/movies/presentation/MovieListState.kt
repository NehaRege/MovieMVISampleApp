package com.example.feature.movies.presentation

import com.example.core.error.AppError
import com.example.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: AppError? = null
)
