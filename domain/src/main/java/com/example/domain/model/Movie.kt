package com.example.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterUrl: String,
)
/*
TODO:
1. Wrap id in a MovieId inline class
2. Replace String date with a proper date type
3. Add helper functions
 */
