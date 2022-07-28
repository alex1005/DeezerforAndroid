package com.alexjprog.deezerforandroid.di

import android.content.Context
import com.alexjprog.deezerforandroid.di.module.CoroutineModule
import com.alexjprog.deezerforandroid.di.module.DataModule
import com.alexjprog.deezerforandroid.di.module.NetworkModule
import com.alexjprog.deezerforandroid.di.module.ViewModelModule
import com.alexjprog.deezerforandroid.ui.mvp.LoginActivity
import com.alexjprog.deezerforandroid.ui.mvp.SearchFragment
import com.alexjprog.deezerforandroid.ui.mvvm.EditorialFragment
import com.alexjprog.deezerforandroid.ui.mvvm.HomeFragment
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
    fun inject(homeFragment: EditorialFragment)
    fun inject(moreContentFragment: MoreContentFragment)
    fun inject(searchFragment: SearchFragment)
}