package com.example.domain.usecase

import com.example.domain.repo.MovieRepository

class GetPopularMoviesUseCase(
    val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int) = repository.getPopularMovies(page)
}