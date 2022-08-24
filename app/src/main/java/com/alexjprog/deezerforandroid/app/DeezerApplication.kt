package com.alexjprog.deezerforandroid.app

import android.app.Application
import androidx.work.*
import com.alexjprog.deezerforandroid.di.AppComponent
import com.alexjprog.deezerforandroid.di.DaggerAppComponent
import com.alexjprog.deezerforandroid.util.EDIT_SELECTION_UPDATE_INTERVAL
import com.alexjprog.deezerforandroid.worker.EditorialWeeklySelectionCheckWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DeezerApplication : Application(), Configuration.Provider {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerConfiguration: Configuration

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        scheduleEditorialWeeklySelectionWorker()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        workerConfiguration

    private fun scheduleEditorialWeeklySelectionWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val worker = PeriodicWorkRequestBuilder<EditorialWeeklySelectionCheckWorker>(
            EDIT_SELECTION_UPDATE_INTERVAL,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .setInitialDelay(EDIT_SELECTION_UPDATE_INTERVAL, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                EditorialWeeklySelectionCheckWorker.UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                worker
            )
    }
}