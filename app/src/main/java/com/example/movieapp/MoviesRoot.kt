package com.example.movieapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.MovieRoutes.DETAIL
import com.example.core.navigation.MovieRoutes.LIST
import com.example.core.navigation.MovieRoutes.SEARCH
import com.example.core.navigation.MovieRoutes.WATCHLIST

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesRoot() {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val showBottomBar = when (currentRoute) {
        DETAIL -> false // usually hide bottom bar on detail
        else -> true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Movies",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == LIST,
                        onClick = {
                            navController.navigate(LIST) {
                                launchSingleTop = true // prevents duplicate destinations
                                restoreState = true // preserves scroll position/state when switching tabs
                                popUpTo(LIST) { saveState = true }
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Movies") },
                        label = { Text("Movies") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == SEARCH,
                        onClick = {
                            navController.navigate(SEARCH) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(LIST) { saveState = true }
                            }
                        },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        label = { Text("Search") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == WATCHLIST,
                        onClick = {
                            navController.navigate(WATCHLIST) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(LIST) { saveState = true }
                            }
                        },
                        icon = { Icon(Icons.Default.Star, contentDescription = "Watchlist") },
                        label = { Text("Watchlist") }
                    )
                }
            }
        }
    ) { innerPadding ->
        MoviesNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}