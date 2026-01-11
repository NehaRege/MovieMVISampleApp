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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feature_movies_presentation.MovieListEvent.ScreenOpened
import com.example.feature_movies_presentation.MovieListPresenter
import com.example.feature_movies_ui.MovieListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesRoot() {
    // 1) Get presenter via our custom factory
    val presenter: MovieListPresenter = viewModel(
        factory = MoviesGraph.movieListPresenterFactory
    )

    // 2) Collect state from presenter
    val state = presenter.state.collectAsStateWithLifecycle()

    // 3) Trigger initial load exactly once
    LaunchedEffect(Unit) {
        presenter.onEvent(ScreenOpened)
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

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
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Movies") },
                    label = {
                        Text("Movies")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = {
                        Text("Search")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Watchlist") },
                    label = {
                        Text("Watchlist")
                    }
                )
            }
        }
    ) { innerPadding ->
        MovieListScreen(
            modifier = Modifier.padding(innerPadding),
            state = state.value,
            onEvent = presenter::onEvent,
            coordinator = presenter.coordinator,
            onNavigateToDetail = { movieId ->
                // TODO: plug into NavController later
                println("Navigate to detail for movieId: $movieId")
            },
            onShowError = { errorMessage ->
                println("Error: $errorMessage")
            }
        )
    }
}