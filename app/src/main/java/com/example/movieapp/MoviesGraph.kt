package com.example.movieapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.remote.MovieApiService
import com.example.data.remote.MoviesRemoteDataSource
import com.example.data.remote.TmdbAuthInterceptor
import com.example.data.repo.MovieRepositoryImpl
import com.example.domain.repo.MovieRepository
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.usecase.GetPopularMoviesUseCase
import com.example.feature_detail_presentation.MovieDetailPresenter
import com.example.feature_movies_presentation.MovieListPresenter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/*
Manual DI Graph.

MovieListPresenter -> GetPopularMoviesUseCase -> MovieRepository -> MoviesRemoteDataSource -> MovieApiService, apiKey (String)
MovieApiService -> Retrofit -> base url, OkHttpClient, Converter factory (Moshi)
OkHttpClient → logging interceptor

*/

object MoviesGraph {
    private val baseUrl = BuildConfig.TMDB_BASE_URL
    private val apiKey: String = BuildConfig.TMDB_API_KEY

    init {
        require(apiKey.isNotBlank()) {
            "TMDB_API_KEY is missing. Add it to local.properties as TMDB_API_KEY=..."
        }
        require(baseUrl.isNotBlank()) {
            "TMDB_BASE_URL is missing. Add it to local.properties as TMDB_BASE_URL=..."
        }
    }

    // --- Network ---
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(TmdbAuthInterceptor(apiKey))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val moviesApi: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }

    // --- Data layer ---
    private val moviesRemoteDataSource: MoviesRemoteDataSource by lazy {
        MoviesRemoteDataSource(
            apiService = moviesApi,
        )
    }

    private val movieRepository: MovieRepository by lazy {
        MovieRepositoryImpl(moviesRemoteDataSource)
    }

    // --- Domain ---
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase by lazy {
        GetPopularMoviesUseCase(movieRepository)
    }

    private val getMovieDetailsUseCase: GetMovieDetailsUseCase by lazy {
        GetMovieDetailsUseCase(movieRepository)
    }

    // --- Presenter factory ---
    val movieListPresenterFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MovieListPresenter::class.java)) {
                    return MovieListPresenter(getPopularMoviesUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class $modelClass")
            }
        }
    }


    val movieDetailPresenterFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MovieDetailPresenter::class.java)) {
                    return MovieDetailPresenter(
                        getMovieDetailsUseCase = getMovieDetailsUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}