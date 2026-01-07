package com.example.data.remote

import okhttp3.Interceptor
import okhttp3.Response

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