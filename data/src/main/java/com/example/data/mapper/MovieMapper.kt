package com.example.data.mapper

import com.example.data.remote.dto.MovieDto
import com.example.data.remote.dto.PopularMoviesResponseDto
import com.example.domain.model.Movie

private const val TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
fun MovieDto.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = posterPath?.let { TMDB_POSTER_BASE_URL + it }.orEmpty(),
        releaseDate = this.releaseDate.orEmpty()
    )
}

fun PopularMoviesResponseDto.toMovieList(): List<Movie> {
    return this.results.map { it.toMovie() }
}
