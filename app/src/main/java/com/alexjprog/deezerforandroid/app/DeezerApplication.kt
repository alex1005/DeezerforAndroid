package com.alexjprog.deezerforandroid.app

import android.app.Application
import androidx.work.*
import com.alexjprog.deezerforandroid.di.AppComponent
import com.alexjprog.deezerforandroid.di.DaggerAppComponent
import com.alexjprog.deezerforandroid.util.*
import com.alexjprog.deezerforandroid.worker.EditorialReleasesCheckWorker
import com.alexjprog.deezerforandroid.worker.EditorialWeeklySelectionCheckWorker
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
        scheduleEditorialReleasesWorker()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        workerConfiguration

    private fun scheduleEditorialWeeklySelectionWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val worker = PeriodicWorkRequestBuilder<EditorialWeeklySelectionCheckWorker>(
            EDIT_SELECTION_UPDATE_INTERVAL,
            EDIT_SELECTION_UPDATE_INTERVAL_TIME_UNIT,
            EDIT_SELECTION_UPDATE_FLEX_INTERVAL,
            EDIT_SELECTION_UPDATE_FLEX_INTERVAL_TIME_UNIT
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                EditorialWeeklySelectionCheckWorker.UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                worker
            )
    }

    private fun scheduleEditorialReleasesWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val worker = PeriodicWorkRequestBuilder<EditorialReleasesCheckWorker>(
            EDIT_RELEASES_UPDATE_INTERVAL,
            EDIT_RELEASES_UPDATE_INTERVAL_TIME_UNIT,
            EDIT_RELEASES_UPDATE_FLEX_INTERVAL,
            EDIT_RELEASES_UPDATE_FLEX_INTERVAL_TIME_UNIT
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                EditorialReleasesCheckWorker.UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                worker
            )
    }
}