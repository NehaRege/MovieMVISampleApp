package com.example.feature_detail_presentation

sealed class MovieDetailCoordinator {
    data object NavigateBack: MovieDetailCoordinator()
    data class ShowError(val message: String): MovieDetailCoordinator()
}