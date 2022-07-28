package com.alexjprog.deezerforandroid.di.module

import androidx.lifecycle.ViewModel
import com.alexjprog.deezerforandroid.viewmodel.EditorialViewModel
import com.alexjprog.deezerforandroid.viewmodel.HomeViewModel
import com.alexjprog.deezerforandroid.viewmodel.MoreContentViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoreContentViewModel::class)
    fun bindMoreContentViewModel(moreContentViewModel: MoreContentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditorialViewModel::class)
    fun bindEditorialViewModel(editorialViewModel: EditorialViewModel): ViewModel
}

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)