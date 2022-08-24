package com.alexjprog.deezerforandroid.di.module

import androidx.work.Configuration
import com.alexjprog.deezerforandroid.worker.factory.UpdateWorkerFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [WorkerModule.WorkerBinds::class])
class WorkerModule {
    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(updateWorkerFactory: UpdateWorkerFactory) =
        Configuration.Builder().setWorkerFactory(updateWorkerFactory).build()

    @Module
    interface WorkerBinds {
    }
}