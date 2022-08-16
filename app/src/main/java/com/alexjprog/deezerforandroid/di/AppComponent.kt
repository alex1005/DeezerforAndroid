package com.alexjprog.deezerforandroid.di

import android.content.Context
import com.alexjprog.deezerforandroid.di.module.*
import com.alexjprog.deezerforandroid.service.MediaPlayerService
import com.alexjprog.deezerforandroid.ui.mvp.LoginActivity
import com.alexjprog.deezerforandroid.ui.mvp.PlayerFragment
import com.alexjprog.deezerforandroid.ui.mvp.SearchFragment
import com.alexjprog.deezerforandroid.ui.mvvm.EditorialFragment
import com.alexjprog.deezerforandroid.ui.mvvm.HomeFragment
import com.alexjprog.deezerforandroid.ui.mvvm.MoreContentFragment
import com.alexjprog.deezerforandroid.ui.mvvm.SearchResultsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class, DataModule::class,
        NetworkModule::class, CoroutineModule::class, PresenterModule::class]
)
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
    fun inject(searchResultsFragment: SearchResultsFragment)
    fun inject(playerFragment: PlayerFragment)

    fun inject(mediaPlayerService: MediaPlayerService)
}