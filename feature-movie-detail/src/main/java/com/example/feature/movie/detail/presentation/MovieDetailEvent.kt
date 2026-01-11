package com.example.feature.movie.detail.presentation

sealed class MovieDetailEvent {
    data object RetryClicked: MovieDetailEvent()
    data object BackClicked: MovieDetailEvent()
    data class ScreenOpened(val movieId: Int): MovieDetailEvent()
}
