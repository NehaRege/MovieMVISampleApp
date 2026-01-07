package com.example.data.remote

import com.example.data.remote.dto.PopularMoviesResponseDto

/*
Interacts with API and returns --> raw DTOs exactly as they come
 */
class MoviesRemoteDataSource(
    val apiService: MovieApiService,
) {
    suspend fun getPopularMovies(page: Int): PopularMoviesResponseDto {
        return apiService.getPopularMovies(
            page = page
        )
    }
}