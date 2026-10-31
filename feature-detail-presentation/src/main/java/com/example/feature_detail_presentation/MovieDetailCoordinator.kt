package com.example.feature_detail_presentation

import com.example.domain.model.DomainError

sealed class MovieDetailCoordinator {
    data object NavigateBack: MovieDetailCoordinator()
    data class ShowError(val error: DomainError): MovieDetailCoordinator()
}