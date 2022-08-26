package com.alexjprog.deezerforandroid.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alexjprog.deezerforandroid.domain.usecase.CheckForNewEditorialWeeklySelectionUseCase
import com.alexjprog.deezerforandroid.util.UpdatesNotificationHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

class EditorialWeeklySelectionCheckWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val checkForNewEditorialWeeklySelectionUseCase: CheckForNewEditorialWeeklySelectionUseCase
) : CoroutineWorker(context, workerParameters) {

    private val updatesNotificationHelper by lazy { UpdatesNotificationHelper(context) }

    override suspend fun doWork(): Result {
        var isError = false
        checkForNewEditorialWeeklySelectionUseCase()
            .catch { isError = true }
            .collectLatest { newAlbums ->
                if (newAlbums.isNotEmpty()) {
                    updatesNotificationHelper.sendEditorialWeeklySelectionUpdateNotification(
                        newAlbums
                    )
                }
            }

        return if (isError) Result.retry() else Result.success()
    }

    companion object {
        const val UNIQUE_NAME = "editorial_weekly_selection_check_worker"
    }
}