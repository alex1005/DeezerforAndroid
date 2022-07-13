package com.alexjprog.deezerforandroid.app

import android.app.Application
import com.alexjprog.deezerforandroid.di.AppComponent
import com.alexjprog.deezerforandroid.di.DaggerAppComponent

class DeezerApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}