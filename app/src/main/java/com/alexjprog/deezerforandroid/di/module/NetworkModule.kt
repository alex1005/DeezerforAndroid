package com.alexjprog.deezerforandroid.di.module

import android.app.Application
import com.alexjprog.deezerforandroid.data.api.DeezerService
import com.alexjprog.deezerforandroid.data.api.interceptor.AuthInterceptor
import com.alexjprog.deezerforandroid.data.sharedprefs.LoginStore
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(loginStore: LoginStore): OkHttpClient =
        OkHttpClient.Builder()
            .also { builder ->
                loginStore.userToken?.let{ builder.addInterceptor(AuthInterceptor(it)) }
            }.build()

    @Singleton
    @Provides
    @Named("logged_in")
    fun provideLoggedInRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideMediaService(@Named("logged_in") retrofit: Retrofit): DeezerService =
        retrofit.create(DeezerService::class.java)
}