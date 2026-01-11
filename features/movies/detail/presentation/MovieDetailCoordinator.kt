package com.example.feature.movie.detail.presentation

sealed class MovieDetailCoordinator {
    data object NavigateBack: MovieDetailCoordinator()
    data class ShowError(val message: String): MovieDetailCoordinator()
}