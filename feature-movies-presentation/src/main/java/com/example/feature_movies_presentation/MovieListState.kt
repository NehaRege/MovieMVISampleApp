package com.example.feature_movies_presentation

import com.example.domain.model.DomainError
import com.example.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: DomainError? = null
)
