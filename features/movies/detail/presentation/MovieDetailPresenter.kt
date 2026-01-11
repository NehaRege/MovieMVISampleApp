package com.example.feature.movie.detail.presentation

import android.arch.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.error.toAppError
import com.example.core.error.toErrorMessage
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.feature.movie.detail.presentation.MovieDetailCoordinator.NavigateBack
import com.example.feature.movie.detail.presentation.MovieDetailEvent.BackClicked
import com.example.feature.movie.detail.presentation.MovieDetailEvent.RetryClicked
import com.example.feature.movie.detail.presentation.MovieDetailEvent.ScreenOpened
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailPresenter(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state

    private val _coordinator = MutableSharedFlow<MovieDetailCoordinator>()
    val coordinator: SharedFlow<MovieDetailCoordinator> = _coordinator

    // store last requested id for retry
    private var lastMovieId: Int? = null

    fun onEvent(event: MovieDetailEvent) {
        when (event) {
            BackClicked -> {
                viewModelScope.launch {
                    _coordinator.emit(NavigateBack)
                }
            }

            RetryClicked -> {
                lastMovieId?.let {
                    loadMovieDetails(it)
                }
            }

            is ScreenOpened -> {
                lastMovieId = event.movieId
                loadMovieDetails(event.movieId)

            }
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId)
                .onStart {
                    _state.update {
                        it.copy(isLoading = true, errorMessage = null)
                    }
                }
                .catch { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.toAppError()
                        )
                    }

                    _coordinator.emit(
                        MovieDetailCoordinator.ShowError(
                            message = throwable.toAppError().toErrorMessage()
                        )
                    )
                }
                .collect { movieDetail ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            movieDetail = movieDetail
                        )
                    }
                }
        }
    }
}