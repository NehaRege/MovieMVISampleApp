package com.example.data.repo

import com.example.data.mapper.toMovie
import com.example.data.mapper.toMovieList
import com.example.data.remote.MoviesRemoteDataSource
import com.example.domain.model.Movie
import com.example.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
Implements Repo from Domain
Communicates with Remote, Local, Cache Data Source and returns --> Flow<List<Movie>>
 */
class MovieRepositoryImpl(
    val moviesRemoteDataSource: MoviesRemoteDataSource,
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): Flow<List<Movie>> = flow {
        // TODO: add loading/error handling if you wrap with Result later
        val moviesDto = moviesRemoteDataSource.getPopularMovies(page)
        val movies = moviesDto.toMovieList()
        emit(movies)
    }

    override suspend fun getMovieDetails(movieId: Int): Flow<Movie> = flow {
        val movieDetailDto = moviesRemoteDataSource.getMovieDetails(movieId)
        val movieDetail = movieDetailDto.toMovie()
        emit(movieDetail)
    }
}