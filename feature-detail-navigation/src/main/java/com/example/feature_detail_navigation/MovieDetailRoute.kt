package com.example.feature_detail_navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_detail_presentation.MovieDetailEvent
import com.example.feature_detail_presentation.MovieDetailPresenter
import com.example.feature_detail_ui.MovieDetailScreen

@Composable
fun MovieDetailRoute(
    factory: ViewModelProvider.Factory,
    movieId: Int,
    onNavigateBack: () -> Unit
) {
    val presenter: MovieDetailPresenter = viewModel(
        factory = factory
    )
    val state = presenter.state.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        presenter.onEvent(MovieDetailEvent.ScreenOpened(movieId))
    }

    MovieDetailScreen(
        state = state.value,
        onEvent = presenter::onEvent,
        coordinator = presenter.coordinator,
        onNavigateBack = onNavigateBack,
        onShowError = { msg -> println("Error: $msg") }
    )
}