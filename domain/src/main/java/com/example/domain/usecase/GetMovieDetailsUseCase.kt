package com.example.domain.usecase

import com.example.domain.repo.MovieRepository

class GetMovieDetailsUseCase(
    val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.getMovieDetails(movieId)
}