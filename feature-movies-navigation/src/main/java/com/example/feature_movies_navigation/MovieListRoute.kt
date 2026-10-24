package com.example.feature_movies_navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_movies_presentation.MovieListEvent.ScreenOpened
import com.example.feature_movies_presentation.MovieListPresenter
import com.example.feature_movies_ui.MovieListScreen

@Composable
fun MovieListRoute(
    factory: ViewModelProvider.Factory,
    onNavigateToDetail: (Int) -> Unit
) {
    // List destination owns the list presenter
    val presenter: MovieListPresenter = viewModel(
        factory = factory
    )
    val state = presenter.state.collectAsStateWithLifecycle()

    // initial load for list destination
    LaunchedEffect(Unit) { presenter.onEvent(ScreenOpened) }

    MovieListScreen(
        state = state.value,
        onEvent = presenter::onEvent,
        coordinator = presenter.coordinator,
        onNavigateToDetail = onNavigateToDetail,
        onShowError = { msg -> println("Error: $msg") }
    )
}
