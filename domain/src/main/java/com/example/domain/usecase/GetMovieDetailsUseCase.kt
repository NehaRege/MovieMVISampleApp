package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.model.Outcome
import com.example.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Outcome<Movie>> = repository.getMovieDetails(movieId)
}