package com.example.data.repo

import com.example.data.error.asOutcome
import com.example.data.mapper.toMovie
import com.example.data.mapper.toMovieList
import com.example.data.remote.MoviesRemoteDataSource
import com.example.domain.model.Movie
import com.example.domain.model.Outcome
import com.example.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
Implements Repo from Domain
Communicates with Remote, Local, Cache Data Source and returns --> Flow<Outcome<...>>
Network exceptions are mapped to DomainError here, so no other layer knows about Retrofit.
 */
class MovieRepositoryImpl(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
) : MovieRepository {
    override fun getPopularMovies(page: Int): Flow<Outcome<List<Movie>>> = flow {
        val moviesDto = moviesRemoteDataSource.getPopularMovies(page)
        val movies = moviesDto.toMovieList()
        emit(movies)
    }.asOutcome()

    override fun getMovieDetails(movieId: Int): Flow<Outcome<Movie>> = flow {
        val movieDetailDto = moviesRemoteDataSource.getMovieDetails(movieId)
        val movieDetail = movieDetailDto.toMovie()
        emit(movieDetail)
    }.asOutcome()
}