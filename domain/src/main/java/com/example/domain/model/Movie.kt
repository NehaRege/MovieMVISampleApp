package com.example.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterUrl: String,
    // detail fields
    val backdropUrl: String? = null,
    val genres: List<String> = emptyList(),
    val rating: Double? = null,
    val tagline: String? = null,
)
/*
TODO:
1. Wrap id in a MovieId inline class
2. Replace String date with a proper date type
3. Add helper functions
 */
