package com.example.data.remote

import com.example.data.remote.dto.PopularMoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): PopularMoviesResponseDto
}