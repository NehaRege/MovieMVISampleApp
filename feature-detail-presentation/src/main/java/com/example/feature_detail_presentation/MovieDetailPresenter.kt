package com.example.feature_detail_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Outcome
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.feature_detail_presentation.MovieDetailCoordinator.NavigateBack
import com.example.feature_detail_presentation.MovieDetailCoordinator.ShowError
import com.example.feature_detail_presentation.MovieDetailEvent.BackClicked
import com.example.feature_detail_presentation.MovieDetailEvent.RetryClicked
import com.example.feature_detail_presentation.MovieDetailEvent.ScreenOpened
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
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
                        it.copy(isLoading = true, error = null)
                    }
                }
                .collect { outcome ->
                    when (outcome) {
                        is Outcome.Success -> _state.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                movieDetail = outcome.value
                            )
                        }

                        is Outcome.Failure -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = outcome.error
                                )
                            }
                            _coordinator.emit(ShowError(error = outcome.error))
                        }
                    }
                }
        }
    }
}