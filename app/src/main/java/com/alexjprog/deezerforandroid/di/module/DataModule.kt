package com.alexjprog.deezerforandroid.di.module

import com.alexjprog.deezerforandroid.data.mapper.DefaultApiMapper
import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.repository.MediaRepositoryImpl
import com.alexjprog.deezerforandroid.data.repository.UserRepositoryImpl
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.api.NetworkDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.sharedprefs.LoginStore
import com.alexjprog.deezerforandroid.data.storage.sharedprefs.LoginStoreImpl
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
        fun bindMediaDataSource(networkDeezerDataSource: NetworkDeezerDataSource): IDeezerDataSource

        @Singleton
        @Binds
        fun bindMediaMapper(defaultApiMapper: DefaultApiMapper): IApiMapper

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