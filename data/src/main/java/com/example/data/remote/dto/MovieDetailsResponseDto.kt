package com.example.data.remote.dto

import com.squareup.moshi.Json

data class MovieDetailsResponseDto(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    val genres: List<GenreDto> = emptyList(),
    @Json(name = "vote_average")
    val voteAverage: Double?,
    val tagline: String?
)

data class GenreDto(
    val id: Int,
    val name: String
)
