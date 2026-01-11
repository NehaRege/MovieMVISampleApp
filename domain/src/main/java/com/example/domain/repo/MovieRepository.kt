package com.example.domain.repo

import com.example.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Flow<List<Movie>>
    suspend fun getMovieDetails(movieId: Int): Flow<Movie>
}