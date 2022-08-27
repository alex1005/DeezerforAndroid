package com.alexjprog.deezerforandroid.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
class CoroutineModule {
    @Singleton
    @Provides
    @Named("api")
    fun provideApiContext(): CoroutineContext = Dispatchers.IO

    @Singleton
    @Provides
    @Named("data")
    fun provideDataContext(): CoroutineContext = Dispatchers.IO
}