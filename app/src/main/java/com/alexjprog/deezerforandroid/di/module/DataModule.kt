package com.alexjprog.deezerforandroid.di.module

import com.alexjprog.deezerforandroid.data.storage.IMediaDataSource
import com.alexjprog.deezerforandroid.data.api.NetworkMediaDataSource
import com.alexjprog.deezerforandroid.data.mapper.DefaultMediaMapper
import com.alexjprog.deezerforandroid.data.mapper.IMediaMapper
import com.alexjprog.deezerforandroid.data.repository.MediaRepositoryImpl
import com.alexjprog.deezerforandroid.data.repository.UserRepositoryImpl
import com.alexjprog.deezerforandroid.data.sharedprefs.LoginStore
import com.alexjprog.deezerforandroid.data.sharedprefs.LoginStoreImpl
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import com.alexjprog.deezerforandroid.domain.repository.UserRepository
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
        fun bindMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository

        @Singleton
        @Binds
        fun bindLoginStore(loginStoreImpl: LoginStoreImpl): LoginStore

        @Singleton
        @Binds
        fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
    }
}