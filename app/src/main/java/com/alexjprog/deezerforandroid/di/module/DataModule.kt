package com.alexjprog.deezerforandroid.di.module

import com.alexjprog.deezerforandroid.data.mapper.DefaultApiMapper
import com.alexjprog.deezerforandroid.data.mapper.DefaultDbMapper
import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.mapper.IDbMapper
import com.alexjprog.deezerforandroid.data.repository.MediaRepositoryImpl
import com.alexjprog.deezerforandroid.data.repository.UserRepositoryImpl
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.ILocalDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.api.NetworkDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.db.LocalDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.sharedprefs.UserStore
import com.alexjprog.deezerforandroid.data.storage.sharedprefs.UserStoreImpl
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
        fun bindDeezerDataSource(networkDeezerDataSource: NetworkDeezerDataSource): IDeezerDataSource

        @Singleton
        @Binds
        fun bindApiMapper(defaultApiMapper: DefaultApiMapper): IApiMapper

        @Singleton
        @Binds
        fun bindMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository

        @Singleton
        @Binds
        fun bindLoginStore(loginStoreImpl: UserStoreImpl): UserStore

        @Singleton
        @Binds
        fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

        @Singleton
        @Binds
        fun bindDbMapper(defaultDbMapper: DefaultDbMapper): IDbMapper

        @Singleton
        @Binds
        fun bindLocalDeezerDataSource(localDeezerDataSource: LocalDeezerDataSource): ILocalDeezerDataSource
    }
}