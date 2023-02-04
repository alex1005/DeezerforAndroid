package com.alexjprog.deezerforandroid.worker.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.alexjprog.deezerforandroid.domain.usecase.CheckForNewEditorialReleasesUseCase
import com.alexjprog.deezerforandroid.worker.EditorialReleasesCheckWorker

class EditorialReleasesCheckWorkerFactory(
    private val checkForNewEditorialReleasesUseCase: CheckForNewEditorialReleasesUseCase
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        when (workerClassName) {
            EditorialReleasesCheckWorker::class.java.name ->
                EditorialReleasesCheckWorker(
                    appContext,
                    workerParameters,
                    checkForNewEditorialReleasesUseCase
                )
            else -> null
        }
}