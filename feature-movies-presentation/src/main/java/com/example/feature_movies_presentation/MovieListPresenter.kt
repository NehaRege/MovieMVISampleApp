package com.example.feature_movies_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.error.toAppError
import com.example.core.error.toErrorMessage
import com.example.domain.usecase.GetPopularMoviesUseCase
import com.example.feature_movies_presentation.MovieListCoordinator.NavigateToMovieDetail
import com.example.feature_movies_presentation.MovieListCoordinator.ShowError
import com.example.feature_movies_presentation.MovieListEvent.MovieClicked
import com.example.feature_movies_presentation.MovieListEvent.RefreshClicked
import com.example.feature_movies_presentation.MovieListEvent.ScreenOpened
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
    val state: StateFlow<MovieListState> = _state // <-- Read Only & Public. UI observes. The UI can only read the state, but the presenter can write to it
    // val state = _state.asStateFlow()
    private val _coordinator =
        MutableSharedFlow<MovieListCoordinator>() // <-- Mutable and Private. Presenter updates
    val coordinator: SharedFlow<MovieListCoordinator> =
        _coordinator // <-- Read Only & Public. UI observes

    /*
    Why not make onEvent() suspend?
        - onEvent represents a synchronous UI event dispatcher.
        - It should not be suspend bc the UI should be able to fire events without caring about coroutine management.
     */
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
                    _state.update {
                        it.copy(isLoading = false, errorMessage = throwable.toAppError())
                    }

                    _coordinator.emit(
                        ShowError(
                            message = throwable.toAppError().toErrorMessage()
                        )
                    )
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
