package com.example.feature_detail_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.error.toMessageRes
import com.example.domain.model.Movie
import com.example.feature_detail_presentation.MovieDetailCoordinator
import com.example.feature_detail_presentation.MovieDetailCoordinator.NavigateBack
import com.example.feature_detail_presentation.MovieDetailCoordinator.ShowError
import com.example.feature_detail_presentation.MovieDetailEvent
import com.example.feature_detail_presentation.MovieDetailState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    state: MovieDetailState,
    onEvent: (MovieDetailEvent) -> Unit,
    coordinator: SharedFlow<MovieDetailCoordinator>,
    onNavigateBack: () -> Unit,
    onShowError: (String) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(coordinator) {
        coordinator.collect { coordinator ->
            when (coordinator) {
                NavigateBack -> onNavigateBack()

                is ShowError -> onShowError(context.getString(coordinator.error.toMessageRes()))
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val movie = state.movieDetail
        val error = state.error
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error != null -> {
                ErrorView(
                    errorMessage = stringResource(error.toMessageRes()),
                    onRetry = { onEvent(MovieDetailEvent.RetryClicked) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            movie != null -> {
                MovieDetailContent(
                    modifier = modifier.fillMaxSize(),
                    title = movie.title,
                    posterUrl = movie.posterUrl,
                    overview = movie.overview,
                    releaseDate = movie.releaseDate,
                    genres = movie.genres,
                    rating = movie.rating,
                    tagline = movie.tagline
                )
            }
        }
    }

}

@Composable
private fun MovieDetailContent(
    modifier: Modifier = Modifier,
    title: String,
    posterUrl: String,
    overview: String,
    releaseDate: String,
    genres: List<String>,
    rating: Double?,
    tagline: String?
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (posterUrl.isNotEmpty()) {
            AsyncImage(
                model = posterUrl,
                contentDescription = "Poster for $title",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = overview,
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))
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

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MovieDetailScreen(
        state = MovieDetailState(
            isLoading = false,
            movieDetail = Movie(
                id = 1,
                title = "Inception",
                overview = "A mind-bending thriller",
                releaseDate = "2010-07-16",
                posterUrl = "https://image.tmdb.org/t/p/w200/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg"
            )
        ),
        onEvent = {},
        coordinator = MutableSharedFlow(),
        onNavigateBack = {},
        onShowError = {}
    )
}