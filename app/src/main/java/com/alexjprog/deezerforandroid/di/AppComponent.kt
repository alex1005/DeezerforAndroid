package com.alexjprog.deezerforandroid.di

import com.alexjprog.deezerforandroid.di.module.CoroutineModule
import com.alexjprog.deezerforandroid.di.module.DataModule
import com.alexjprog.deezerforandroid.di.module.NetworkModule
import com.alexjprog.deezerforandroid.di.module.ViewModelModule
import com.alexjprog.deezerforandroid.ui.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, DataModule::class,
    NetworkModule::class, CoroutineModule::class])
interface AppComponent {
    fun inject(homeFragment: HomeFragment)
}