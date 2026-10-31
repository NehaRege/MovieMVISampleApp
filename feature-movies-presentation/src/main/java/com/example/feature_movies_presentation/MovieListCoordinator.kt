package com.example.feature_movies_presentation

import com.example.domain.model.DomainError

/*
Effect or Coordinator

Effects represent one-off events that:
    - should not be replayed as part of the persistent screen state
    - are usually about navigation / snackbars / toasts / dialogs

They differ from State because state is continuous (you can reconstruct the UI from it), while effects are fire-and-forget.
 */
sealed class MovieListCoordinator {
    data class NavigateToMovieDetail(val movieId: Int): MovieListCoordinator()
    data class ShowError(val error: DomainError): MovieListCoordinator()
}