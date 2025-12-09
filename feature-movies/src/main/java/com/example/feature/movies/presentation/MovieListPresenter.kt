package com.example.feature.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetPopularMoviesUseCase
import com.example.feature.movies.presentation.MovieListCoordinator.NavigateToMovieDetail
import com.example.feature.movies.presentation.MovieListCoordinator.ShowError
import com.example.feature.movies.presentation.MovieListEvent.MovieClicked
import com.example.feature.movies.presentation.MovieListEvent.RefreshClicked
import com.example.feature.movies.presentation.MovieListEvent.ScreenOpened
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListPresenter(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {
    private val _state =
        MutableStateFlow(MovieListState()) // <-- Mutable & Private. Presenter updates
    val state: StateFlow<MovieListState> = _state // <-- Read Only & Public. UI observes

    private val _coordinator =
        MutableSharedFlow<MovieListCoordinator>() // <-- Mutable and Private. Presenter updates
    val coordinator: SharedFlow<MovieListCoordinator> =
        _coordinator // <-- Read Only & Public. UI observes

    fun onEvent(event: MovieListEvent) {
        when (event) {
            is MovieClicked -> {
                handleMovieClicked(movieId = event.movieId)
            }

            RefreshClicked,
            ScreenOpened -> {
                loadMovies()
            }
        }
    }

    private fun loadMovies(page: Int = 1) {
        viewModelScope.launch {
            getPopularMoviesUseCase(page = page)
                .onStart {
                    // Operator executed before the first emission of the flow.
                    _state.update {
                        it.copy(isLoading = true, errorMessage = null)
                    }
                }
                .catch { throwable ->
                    // Catches exceptions thrown upstream in the flow
                    val errorMessage =
                        throwable.message ?: "Oops something went wrong. Please try again."
                    _state.update {
                        it.copy(isLoading = false, errorMessage = errorMessage)
                    }

                    _coordinator.emit(ShowError(message = errorMessage))
                }
                .collect { movies ->
                    // Called for each emission.
                    _state.update {
                        it.copy(isLoading = false, errorMessage = null, movies = movies)
                    }
                }
        }
    }

    private fun handleMovieClicked(movieId: Int) {
        viewModelScope.launch {
            _coordinator.emit(NavigateToMovieDetail(movieId = movieId))
        }
    }
}
