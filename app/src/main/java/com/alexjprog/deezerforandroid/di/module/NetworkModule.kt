package com.alexjprog.deezerforandroid.di.module

import com.alexjprog.deezerforandroid.data.api.MediaService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideMediaService(retrofit: Retrofit): MediaService =
        retrofit.create(MediaService::class.java)
}