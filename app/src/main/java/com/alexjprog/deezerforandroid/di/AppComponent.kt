package com.alexjprog.deezerforandroid.di

import android.app.Application
import com.alexjprog.deezerforandroid.di.module.*
import com.alexjprog.deezerforandroid.ui.HomeFragment
import com.alexjprog.deezerforandroid.ui.MoreContentFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, DataModule::class,
    NetworkModule::class, CoroutineModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun inject(homeFragment: HomeFragment)
    fun inject(moreContentFragment: MoreContentFragment)
}