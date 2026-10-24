package com.example.movieapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.core.navigation.MovieRoutes
import com.example.feature_detail_navigation.MovieDetailRoute
import com.example.feature_movies_navigation.MovieListRoute

/*
NavHost --> Container on the screen that swaps destinations
        --> Define a START destination & a set of destinations
 */
@Composable
fun MoviesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MovieRoutes.LIST,
        modifier = modifier
    ) {
        composable(MovieRoutes.LIST) {
            MovieListRoute(
                factory = MoviesGraph.movieListPresenterFactory,
                onNavigateToDetail = { movieId ->
                    navController.navigate(MovieRoutes.detail(movieId))
                }
            )
        }

        composable(
            route = MovieRoutes.DETAIL,
            arguments = listOf(
                navArgument(MovieRoutes.MOVIE_ID_ARG) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(MovieRoutes.MOVIE_ID_ARG)
                ?: error("movieId is required")

            MovieDetailRoute(
                factory = MoviesGraph.movieDetailPresenterFactory,
                movieId = movieId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(MovieRoutes.SEARCH) {
            // placeholder
            Text("Search")
        }

        composable(MovieRoutes.WATCHLIST) {
            // placeholder
            Text("Watchlist")
        }
    }
}