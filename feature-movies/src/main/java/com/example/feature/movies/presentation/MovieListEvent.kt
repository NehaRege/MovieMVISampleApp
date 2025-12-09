package com.example.feature.movies.presentation

sealed class MovieListEvent {
    data object ScreenOpened: MovieListEvent()
    data object RefreshClicked: MovieListEvent()
    data class MovieClicked(val movieId: Int): MovieListEvent()
}