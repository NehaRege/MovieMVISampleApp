package com.example.domain.repo

import com.example.domain.model.Movie
import com.example.domain.model.Outcome
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(page: Int): Flow<Outcome<List<Movie>>>
    fun getMovieDetails(movieId: Int): Flow<Outcome<Movie>>
}