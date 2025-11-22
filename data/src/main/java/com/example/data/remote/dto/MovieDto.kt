package com.example.data.remote.dto

import com.squareup.moshi.Json

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "poster_path")
    val posterPath: String?
)
