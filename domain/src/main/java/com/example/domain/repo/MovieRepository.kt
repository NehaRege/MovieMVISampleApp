package com.example.domain.repo

import com.example.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(page: Int): Flow<List<Movie>>
    fun getMovieDetails(movieId: Int): Flow<Movie>
}