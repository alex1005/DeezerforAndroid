package com.alexjprog.deezerforandroid.di.module

import android.content.Context
import androidx.room.Room
import com.alexjprog.deezerforandroid.data.storage.db.DeezerLocalDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    @Singleton
    @Provides
    fun provideDb(applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            DeezerLocalDb::class.java, "deezer_local.db"
        ).build()

    @Singleton
    @Provides
    fun provideQueryHistoryDao(db: DeezerLocalDb) =
        db.getQueryHistoryDao()
}