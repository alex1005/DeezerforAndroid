package com.alexjprog.deezerforandroid.di

import android.content.Context
import com.alexjprog.deezerforandroid.di.module.*
import com.alexjprog.deezerforandroid.ui.mvvm.HomeFragment
import com.alexjprog.deezerforandroid.ui.mvp.LoginActivity
import com.alexjprog.deezerforandroid.ui.mvp.SearchFragment
import com.alexjprog.deezerforandroid.ui.mvvm.MoreContentFragment
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
        fun application(context: Context): Builder
    }

    fun inject(loginActivity: LoginActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(moreContentFragment: MoreContentFragment)
    fun inject(searchFragment: SearchFragment)
}