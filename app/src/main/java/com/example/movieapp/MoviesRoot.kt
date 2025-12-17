package com.example.movieapp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature.movies.presentation.MovieListEvent.ScreenOpened
import com.example.feature.movies.presentation.MovieListPresenter
import com.example.feature.movies.ui.MovieListScreen

@Composable
fun MoviesRoot() {
    // 1) Get presenter via our custom factory
    val presenter: MovieListPresenter = viewModel(
        factory = MoviesGraph.movieListPresenterFactory
    )

    // 2) Collect state from presenter
    val state = presenter.state.collectAsStateWithLifecycle()

    // 3) Trigger initial load exactly once
    LaunchedEffect(Unit) {
        presenter.onEvent(ScreenOpened)
    }

    MovieListScreen(
        state = state.value,
        onEvent = presenter::onEvent,
        coordinator = presenter.coordinator,
        onNavigateToDetail = { movieId ->
            // TODO: plug into NavController later
            println("Navigate to detail for movieId: $movieId")
        },
        onShowError = { errorMessage ->
            println("Error: $errorMessage")
        }
    )
}