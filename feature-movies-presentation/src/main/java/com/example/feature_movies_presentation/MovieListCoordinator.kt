package com.example.feature_movies_presentation

// Effect or Coordinator
sealed class MovieListCoordinator {
    data class NavigateToMovieDetail(val movieId: Int): MovieListCoordinator()
    data class ShowError(val message: String): MovieListCoordinator()
}