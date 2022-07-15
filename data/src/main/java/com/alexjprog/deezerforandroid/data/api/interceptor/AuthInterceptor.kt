package com.alexjprog.deezerforandroid.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val accessToken: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestUrl = request.url()
            .newBuilder()
            .addQueryParameter(ACCESS_TOKEN_PARAM_KEY, accessToken)
            .build()
        val newRequest = request.newBuilder()
            .url(newRequestUrl)
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val ACCESS_TOKEN_PARAM_KEY = "access_token"
    }
}