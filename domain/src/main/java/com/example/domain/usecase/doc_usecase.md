
**What is a UseCase?**
A single, focused action your app can perform → a business rule.
 - GetPopularMovies
 - GetMovieDetails
 - SearchMovies
 - DeleteFavoriteMovie
 - AddMovieToFavorites

**Why UseCase?**
Clean separation between “What the app does” and “How it does it”
 - UI or ViewModel says → “I want movie details.”
 - UseCase says → “Here’s the business logic to get movie details.”
 - Repository → handles where it actually comes from (remote, local, cache).

**Keywords**
suspend
    - marks a function as suspendable - meaning it can pause its execution without blocking the thread and resume later.
    - Fetching movie data is a long-running operation. Marking fun as “suspend” allows it to be called from a coroutine making it non-blocking.

Operator fun invoke
    "operator" keyword → allows you to define special behavior for operators in Kotlin.
    "invoke()" function → When you define "operator fun invoke(..)", you can call an instance of the class as if it were a function.


Note: If the list grows huge, group related use cases in a single file for ergonomics, but I prefer one class per core operation for clarity and testability.
