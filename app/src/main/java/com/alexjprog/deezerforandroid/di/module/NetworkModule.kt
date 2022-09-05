package com.alexjprog.deezerforandroid.di.module


import com.alexjprog.deezerforandroid.data.storage.api.DeezerService
import com.alexjprog.deezerforandroid.data.storage.api.interceptor.AuthInterceptor
import com.alexjprog.deezerforandroid.data.storage.sharedprefs.UserStore
import com.alexjprog.deezerforandroid.util.NETWORK_DATE_FORMAT_PATTERN
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(userStore: UserStore): OkHttpClient =
        OkHttpClient.Builder()
            .also { builder ->
                builder.addInterceptor(AuthInterceptor(userStore))
            }.build()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create(
            GsonBuilder()
                .setDateFormat(NETWORK_DATE_FORMAT_PATTERN)
                .create()
        )

    @Singleton
    @Provides
    fun provideLoggedInRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Singleton
    @Provides
    fun provideMediaService(retrofit: Retrofit): DeezerService =
        retrofit.create(DeezerService::class.java)
}