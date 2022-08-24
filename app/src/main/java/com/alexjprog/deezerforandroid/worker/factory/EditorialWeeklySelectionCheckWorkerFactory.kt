package com.alexjprog.deezerforandroid.worker.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.alexjprog.deezerforandroid.domain.usecase.CheckForNewEditorialWeeklySelectionUseCase
import com.alexjprog.deezerforandroid.worker.EditorialWeeklySelectionCheckWorker

class EditorialWeeklySelectionCheckWorkerFactory(
    private val checkForNewEditorialWeeklySelectionUseCase: CheckForNewEditorialWeeklySelectionUseCase
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        when (workerClassName) {
            EditorialWeeklySelectionCheckWorker::class.java.name ->
                EditorialWeeklySelectionCheckWorker(
                    appContext,
                    workerParameters,
                    checkForNewEditorialWeeklySelectionUseCase
                )
            else -> null
        }
}