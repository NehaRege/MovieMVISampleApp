package com.example.feature_movies_presentation

sealed class MovieListEvent {
    data object ScreenOpened: MovieListEvent()
    data object RefreshClicked: MovieListEvent()
    data class MovieClicked(val movieId: Int): MovieListEvent()
}