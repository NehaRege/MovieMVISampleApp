package com.example.feature_detail_presentation

sealed class MovieDetailEvent {
    data object RetryClicked: MovieDetailEvent()
    data object BackClicked: MovieDetailEvent()
    data class ScreenOpened(val movieId: Int): MovieDetailEvent()
}
