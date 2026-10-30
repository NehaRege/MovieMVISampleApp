package com.example.domain.usecase

import com.example.domain.repo.MovieRepository

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int) = repository.getMovieDetails(movieId)
}