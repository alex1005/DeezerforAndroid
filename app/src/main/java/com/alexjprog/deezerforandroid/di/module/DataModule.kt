package com.alexjprog.deezerforandroid.di.module

import com.alexjprog.deezerforandroid.data.api.IMediaDataSource
import com.alexjprog.deezerforandroid.data.api.NetworkMediaDataSource
import com.alexjprog.deezerforandroid.data.mapper.DefaultMediaMapper
import com.alexjprog.deezerforandroid.data.mapper.IMediaMapper
import com.alexjprog.deezerforandroid.data.repository.MediaRepositoryImpl
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [DataModule.DataBinds::class])
class DataModule {
    @Module
    interface DataBinds {
        @Singleton
        @Binds
        fun bindMediaDataSource(networkMediaDataSource: NetworkMediaDataSource): IMediaDataSource

        @Singleton
        @Binds
        fun bindMediaMapper(defaultMediaMapper: DefaultMediaMapper): IMediaMapper

        @Singleton
        @Binds
        fun bindRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository
    }
}