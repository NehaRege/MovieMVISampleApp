package com.example.data.remote

import com.example.data.remote.dto.PopularMoviesResponseDto

/*
Interacts with API and returns --> raw DTOs exactly as they come
 */
class MoviesRemoteDataSource(
    val apiService: MovieApiService,
    val apiKey: String
) {
    suspend fun getPopularMovies(page: Int): PopularMoviesResponseDto {
        return apiService.getPopularMovies(
            apiKey = apiKey,
            page = page
        )
    }
}