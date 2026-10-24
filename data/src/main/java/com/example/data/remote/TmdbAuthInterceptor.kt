package com.example.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/*
TMDB requires an API key to be attached to every network request.
Instead of passing the key through each API method, we centralize authentication using an OkHttp interceptor.

This ensures authentication is:
    - applied consistently
    - implemented once
    - easy to change or remove later

Why an interceptor?
Authentication is a cross-cutting HTTP concern, not a domain or feature concern.
Using an interceptor:
    - prevents forgetting to add api_key on new endpoints
    - keeps Retrofit APIs clean and focused on request data
    - avoids duplicating auth logic across data sources
    - allows future auth changes (e.g. Bearer token) in one place
 */
class TmdbAuthInterceptor(
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newUrl = original.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}