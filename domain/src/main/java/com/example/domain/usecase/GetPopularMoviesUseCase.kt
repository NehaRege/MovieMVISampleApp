package com.example.domain.usecase

import com.example.domain.repo.MovieRepository

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
    val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int) = repository.getPopularMovies(page)
    /*
    suspend fun execute(page: Int) = repository.getPopularMovies(page)
    getPopularMoviesUseCase.execute(1) <-- this is how u call from ViewModel
     */

}

/*
suspend
    - marks a function as suspendable - meaning it can pause its execution without blocking the thread and resume later.
    - Fetching movie data is a long-running operation. Marking fun as “suspend” allows it to be called from a coroutine making it non-blocking.

Operator fun invoke
    "operator" keyword → allows you to define special behavior for operators in Kotlin.
    "invoke()" function → When you define "operator fun invoke(..)", you can call an instance of the class as if it were a function.


Without operator fun invoke, you'll have to write
    suspend fun execute(page: Int) = repository.getPopularMovies(page)

And then call it like --> getPopularMoviesUseCase.execute(1)

With operator fun invoke --> you can directly call getPopularMoviesUseCase(1)
 */