package com.alexjprog.deezerforandroid.data.api.interceptor

import com.alexjprog.deezerforandroid.data.sharedprefs.LoginStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val loginStore: LoginStore): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = loginStore.userToken
        return if(token == null) chain.proceed(request)
        else {
            val newRequestUrl = request.url()
                .newBuilder()
                .addQueryParameter(ACCESS_TOKEN_PARAM_KEY, token)
                .build()
            val newRequest = request.newBuilder()
                .url(newRequestUrl)
                .build()
            chain.proceed(newRequest)
        }
    }

    companion object {
        const val ACCESS_TOKEN_PARAM_KEY = "access_token"
    }
}