package com.example.feature.movies.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.feature.movies.presentation.MovieListCoordinator
import com.example.feature.movies.presentation.MovieListCoordinator.NavigateToMovieDetail
import com.example.feature.movies.presentation.MovieListCoordinator.ShowError
import com.example.feature.movies.presentation.MovieListEvent
import com.example.feature.movies.presentation.MovieListEvent.MovieClicked
import com.example.feature.movies.presentation.MovieListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    modifier: Modifier = Modifier,
    state: MovieListState,
    onEvent: (MovieListEvent) -> Unit,
    coordinator: SharedFlow<MovieListCoordinator>,
    onNavigateToDetail: (Int) -> Unit,
    onShowError: (String) -> Unit
) {
    LaunchedEffect(coordinator) {
        coordinator.collect { coordinator ->
            when (coordinator) {
                is NavigateToMovieDetail -> {
                    onNavigateToDetail(coordinator.movieId)
                }

                is ShowError -> {
                    onShowError(coordinator.message)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.errorMessage != null -> {
                ErrorView(
                    errorMessage = state.errorMessage
                        ?: "Oops! An error occurred. Please refresh or try again later.",
                    onRetry = { onEvent(MovieListEvent.RefreshClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                MovieList(
                    movieList = state.movies,
                    onMovieClicked = { movieId ->
                        onEvent(MovieClicked(movieId = movieId))
                    }
                )
            }
        }
    }
}

@Composable
fun MovieList(
    movieList: List<Movie>,
    onMovieClicked: (Int) -> Unit
) {
    LazyColumn {
        items(movieList) { movie ->
            MovieItem(
                movie = movie,
                onMovieClicked = onMovieClicked
            )
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClicked: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 10.dp,
                horizontal = 16.dp
            )
            .clickable {
                onMovieClicked(movie.id)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Add coil dependency for AsyncImage in gradle & libs.version
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = "Poster for ${movie.title}",
                modifier = Modifier
                    .size(width = 72.dp, height = 100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Release: ${movie.releaseDate.take(4)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}

private fun dummyMoviesList() = listOf(
    Movie(
        id = 1,
        title = "Inception",
        overview = "A mind-bending thriller",
        releaseDate = "2010-07-16",
        posterUrl = "https://image.tmdb.org/t/p/w200/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg"
    ),
    Movie(
        id = 2,
        title = "Interstellar",
        overview = "A journey beyond the stars",
        releaseDate = "2014-11-07",
        posterUrl = "https://image.tmdb.org/t/p/w200/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg"
    )
)

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    MovieListScreen(
        state = MovieListState(
            isLoading = false,
            movies = dummyMoviesList(),
            errorMessage = null
        ),
        onEvent = {},
        coordinator = MutableSharedFlow(),
        onNavigateToDetail = {},
        onShowError = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    MovieListScreen(
        state = MovieListState(
            errorMessage = "Something went wrong"
        ),
        onEvent = {},
        coordinator = MutableSharedFlow(),
        onNavigateToDetail = {},
        onShowError = {}
    )
}