package com.example.core.navigation

/*
These are the destinations --> "screen addresses"
 */
object MovieRoutes {
    const val LIST = "movie_list"

    const val MOVIE_ID_ARG = "movieId"
    const val DETAIL = "movie_detail/{$MOVIE_ID_ARG}"

    const val SEARCH = "movie_search"
    const val WATCHLIST = "movie_watchlist"

    fun detail(movieId: Int): String = "movie_detail/$movieId"
}