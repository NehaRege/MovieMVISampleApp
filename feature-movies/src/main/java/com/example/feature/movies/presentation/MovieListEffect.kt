package com.example.feature.movies.presentation

sealed class MovieListEffect {
    data class NavigateToMovieDetail(val movieId: Int): MovieListEffect()
    data class ShowError(val message: String): MovieListEffect()
}