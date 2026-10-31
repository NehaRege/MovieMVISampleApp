package com.example.domain.usecase

import com.example.domain.model.Movie
import com.example.domain.model.Outcome
import com.example.domain.repo.MovieRepository
import kotlinx.coroutines.flow.Flow

/*
A single, focused action your app can perform → a business rule.
 - GetPopularMovies
 - GetMovieDetails
 - SearchMovies
 - DeleteFavoriteMovie
 - AddMovieToFavorites

Why UseCase? Clean separation between “What the app does” and “How it does it”
 - UI or ViewModel says → “I want movie details.”
 - UseCase says → “Here’s the business logic to get movie details.”
 - Repository → handles where it actually comes from (remote, local, cache).
 */
class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    operator fun invoke(page: Int): Flow<Outcome<List<Movie>>> = repository.getPopularMovies(page)
    /*
    fun execute(page: Int) = repository.getPopularMovies(page)
    getPopularMoviesUseCase.execute(1) <-- this is how u call from ViewModel
     */
}

/*
Operator fun invoke
    "operator" keyword → allows you to define special behavior for operators in Kotlin.
    "invoke()" function → When you define "operator fun invoke(..)", you can call an instance of the class as if it were a function.


Without operator fun invoke, you'll have to write
    fun execute(page: Int) = repository.getPopularMovies(page)

And then call it like --> getPopularMoviesUseCase.execute(1)

With operator fun invoke --> you can directly call getPopularMoviesUseCase(1)
 */